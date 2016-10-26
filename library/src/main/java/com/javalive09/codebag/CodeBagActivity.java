package com.javalive09.codebag;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * Created by peter on 16/9/21.
 */
public class CodeBagActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static List<CodeBagActivity> mActContainer = new LinkedList<>();
    static Map<String, ArrayList<Object>> mObjectMap = new HashMap<>();
    private Node mCurrentNode;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActContainer.add(CodeBagActivity.this);
        Intent intent = getIntent();
        if (intent != null) {
            Node node = intent.getParcelableExtra(CodeBag.NODE_NAME);
            if (node == null) {//init
                CodeBag app = (CodeBag) getApplication();
                node = app.init();
            }
            mCurrentNode = node;
            if (mCurrentNode.type == Node.CLASS) {
                String className = mCurrentNode.className;
                ArrayList<Object> mObjectList = mObjectMap.get(className);
                if (mObjectList == null) {
                    mObjectList = new ArrayList<>();
                }
                Class<?> cls;
                try {
                    cls = Class.forName(className);
                    Constructor<?> con = cls.getConstructor();
                    Object obj = con.newInstance();
                    if (obj != null) {
                        Field mActivity = cls.getSuperclass().getDeclaredField("mBaseActivity");
                        mActivity.setAccessible(true);
                        mActivity.set(obj, CodeBagActivity.this);
                        mObjectList.add(obj);
                        mObjectMap.put(className, mObjectList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            initActionBar(node);
            initStatusBar();
            setContentView(R.layout.activity_main_view);
            mListView = (ListView) findViewById(R.id.lv);
            ListAdapter adapter = new ListAdapter(CodeBagActivity.this, node.mSubNodeList);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CodeBag.NODE_NAME, mCurrentNode);
    }

    private ArrayList<Node> getMethodNodes(Node parentNode) {
        ArrayList<Node> methodList = new ArrayList<>();
        try {
            String className = parentNode.className;
            Class<?> cls = Class.forName(className);
            String superClassName = cls.getSuperclass().getSimpleName();
            if (superClassName.equals(Entry.class.getSimpleName())) {
                Method[] methods = cls.getDeclaredMethods();
                for (Method m : methods) {
                    if (Modifier.PUBLIC == m.getModifiers()
                            && m.getParameterTypes().length == 0) {
                        String methodName = m.getName();
                        Node node = new Node(methodName, Node.METHOD, className);
                        methodList.add(node);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return methodList;
    }

    private void initStatusBar() {
        int ver = android.os.Build.VERSION.SDK_INT;
        if (ver >= 21) {
            new StatusBarApiInvoke(this).invoke();
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag(R.id.main_item_pos);
        Node node = (Node) mListView.getAdapter().getItem(position);
        if (node != null) {
            switch (node.type) {
                case Node.DIR:
                    Intent intent = new Intent(CodeBagActivity.this, CodeBagActivity.class);
                    intent.putExtra(CodeBag.NODE_NAME, node);
                    startActivity(intent);
                    break;
                case Node.CLASS:
                    ArrayList<Node> nodeList = getMethodNodes(node);
                    if(nodeList != null && nodeList.size() > 0) {
                        node.mSubNodeList = getMethodNodes(node);
                        intent = new Intent(CodeBagActivity.this, CodeBagActivity.class);
                        intent.putExtra(CodeBag.NODE_NAME, node);
                        startActivity(intent);
                    }
                    break;
                case Node.METHOD:
                    intent = new Intent(CodeBagActivity.this, ShowViewActivity.class);
                    intent.putExtra(CodeBag.NODE_NAME, node);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private String getRootUrl() {
        int ownerStrId = getResources().getIdentifier("git_owner", "string", getPackageName());
        if (ownerStrId != 0) {
            int repoStrId = getResources().getIdentifier("git_repo", "string", getPackageName());
            if (repoStrId != 0) {
                int dirStrId = getResources().getIdentifier("git_dir", "string", getPackageName());
                if (dirStrId != 0) {
                    String owner = getString(ownerStrId);
                    String repo = getString(repoStrId);
                    String rootDir = getString(dirStrId);
                    return CodeBag.GIT_HUB_HOME + owner + "/" + repo + "/master/" + rootDir + "/src/main/java/";
                } else {
                    Toast.makeText(CodeBagActivity.this, R.string.no_dir, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CodeBagActivity.this, R.string.no_repo, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CodeBagActivity.this, R.string.no_owner, Toast.LENGTH_LONG).show();
        }
        return "";
    }


    @Override
    public boolean onLongClick(View v) {
        int position = (Integer) v.getTag(R.id.main_item_pos);
        Node node = (Node) mListView.getAdapter().getItem(position);
        if (node.type == Node.CLASS) {//get code source
            final AlertDialog dialog = showAlertDialog(node.name + ".java", getString(R.string.loading));
            String path = node.className.replace(".", "/") + ".java";
            String url = getRootUrl();
            if (!TextUtils.isEmpty(url)) {
                final AsyncTask loading = getGitHubCode(dialog, getRootUrl() + path);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loading.cancel(true);
                    }
                });
                return true;
            }
        }
        return false;
    }

    private AsyncTask getGitHubCode(final AlertDialog dialog, final String url) {
        return new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                HttpURLConnection c = null;
                StringBuilder sb = new StringBuilder();
                try {
                    URL u = new URL(url);
                    c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.setConnectTimeout(10 * 1000);
                    c.setReadTimeout(10 * 1000);
                    c.connect();
                    int status = c.getResponseCode();
                    if (status == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                            sb.append("\n");
                        }
                        br.close();
                    } else {
                        showToast(R.string.no_code);
                    }
                } catch (java.net.SocketTimeoutException e) {
                    e.printStackTrace();
                    showToast(R.string.time_out);
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast(R.string.io_exp);
                } finally {
                    if (c != null) {
                        try {
                            c.disconnect();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                return sb.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (!TextUtils.isEmpty(s)) {
                    updateContent(dialog, s);
                }
            }
        }.execute();
    }

    private void showToast(final int resStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CodeBagActivity.this, resStr, Toast.LENGTH_LONG).show();
            }
        });
    }

    private static class StatusBarApiInvoke {
        Activity mAct;

        StatusBarApiInvoke(Activity act) {
            mAct = act;
        }

        @TargetApi(21)
        void invoke() {
            Window window = mAct.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(mAct.getResources().getColor(R.color.statusbar));
        }
    }

    private void initActionBar(Node node) {
        setOverflowShowingAlways();
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            String name = node.name;
            switch (node.type) {
                case Node.DIR:
                    break;
                case Node.CLASS:
                    name += ".java";
                    break;
                case Node.METHOD:
                    name += "( )";
                    break;
            }
            getSupportActionBar().setTitle(name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActContainer.remove(CodeBagActivity.this);

        ArrayList<Object> mObjectList = mObjectMap.get(mCurrentNode.className);
        if(mObjectList != null) {
            int len = mObjectList.size();
            if(len > 1) {
                mObjectList.remove(len - 1);
            }else {
                mObjectList.clear();
                mObjectMap.remove(mCurrentNode.className);
            }
            LogUtil.e("mObjectList =" + mObjectList.toString() + ";map = " + mObjectMap.toString());
        }

    }

    private void exit() {
        for (CodeBagActivity act : mActContainer) {
            act.finish();
        }
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_help) {
            showAlertDialog(getString(R.string.action_help),
                    getString(R.string.action_help_msg));
        } else if (id == R.id.action_about) {
            showAlertDialog(getString(R.string.action_about),
                    getString(R.string.action_about_msg));
        } else if (id == R.id.action_showlog) {
            showAlertDialog("log", LogUtil.getLog());
        } else if (id == R.id.action_clearlog) {
            LogUtil.clearLog();
        } else if (id == R.id.action_exit) {
            exit();
        }
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog showAlertDialog(String title, String content) {
        AlertDialog dialog = new AlertDialog.Builder(CodeBagActivity.this,
                R.style.AppCompatAlertDialogStyle).create();
        dialog.setCanceledOnTouchOutside(true);
        View dialogView = View.inflate(CodeBagActivity.this, R.layout.alertdialog_view, null);
        dialog.setView(dialogView);
        TextView titleView = (TextView) dialogView.findViewById(R.id.title);
        TextView contentView = (TextView) dialogView.findViewById(R.id.content);
        titleView.setText(title);
        contentView.setText(content);
        dialog.show();
        return dialog;
    }

    private void updateContent(AlertDialog dialog, String content) {
        TextView contentView = (TextView) dialog.findViewById(R.id.content);
        contentView.setText(content);
        dialog.show();
    }

}