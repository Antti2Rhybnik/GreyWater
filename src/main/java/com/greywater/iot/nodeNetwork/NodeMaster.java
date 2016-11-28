package com.greywater.iot.nodeNetwork;

import javax.script.Compilable;
import javax.script.ScriptEngineManager;

/**
 * Created by alexander on 28.11.16.
 */
public class NodeMaster {
    public static Compilable engine = (Compilable) new ScriptEngineManager().getEngineByName("javascript");

}
