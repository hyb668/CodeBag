package com.codebag.code.mycode.pattern.behavioral.observer;

import com.codebag.bag.Entry;
import com.codebag.bag.MyCode;
import com.codebag.bag.main.InovkedViewActivity;

/**
 * 一个对象状态的改变能影响一系列对象状态的改变
 * 
 * @author zhangrui-ms
 *
 */
public class Invoker extends MyCode {

    public Invoker(InovkedViewActivity act) {
        super(act);
    }
    
    @Entry
    public void invoke() {
        Obserable obserable = new Obserable();
        Observer a = new ObserverA();
        Observer b = new ObserverB();
        obserable.addObserver(a);
        obserable.addObserver(b);
        obserable.actionAll();
    }

}
