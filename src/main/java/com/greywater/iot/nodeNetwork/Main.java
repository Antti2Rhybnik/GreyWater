package com.greywater.iot.nodeNetwork;


import javax.script.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Compilable engine = (Compilable) new ScriptEngineManager().getEngineByName("javascript");

    public static void main(String[] args) {

        try {


            Bindings bindings = new SimpleBindings();
            bindings.put("_1", 39);
            bindings.put("_2", 1);

            String fr = "_1 > _2";

            Boolean res = false;


                System.out.println("Compiling....");
                Compilable compEngine = (Compilable)engine;
                CompiledScript cs = compEngine.compile(fr);
                res = (Boolean) cs.eval(bindings);


            System.out.println(res+"");

        }
        catch(ScriptException scrEx)
        {
            scrEx.printStackTrace();
        }
    }


    void method() {

        List<Node> allNodes = new ArrayList<>();
        List<SensorNode> sensorNodes = new ArrayList<>();
        List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();



    }

}
