package com.example.jmsf1.model;
import lombok.*;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class F1Car {

    private float engineTemp;
    private float tyresPress;
    private float oilPress;

    public static final double engineTempMax=120;
    public static final double engineTempMin=110;
    public static final double tyresPressMax=1.2;
    public static final double tyresPressMin=1.0;
    public static final double oilPressMax=40;
    public static final double oilPressMin=20;


    public static ArrayList getF1CarParameters(){
       ArrayList<Double> carParameters = new ArrayList<>();

        double engineTemp = getRandomParameter(122,108);
        carParameters.add(engineTemp);
        carParameters.add(isParameterValueExceeded(engineTemp,"engineTemp"));

        double tyresPress = getRandomParameter(1.3,0.9);
        carParameters.add(isParameterValueExceeded(tyresPress,"tyresPress"));
        carParameters.add(tyresPress);

        double oilPress = getRandomParameter(42,18);
        carParameters.add(oilPress);
        carParameters.add(isParameterValueExceeded(oilPress,"oilPress"));

        return carParameters;
    }


    private static double getRandomParameter(double max, double min){
        double parameter = min + Math.random() * (max  - min);
        parameter=(double)Math.round(parameter * 1000d) / 1000d;
        return parameter;
    }

    private static double isParameterValueExceeded(double value, String parameter){
        switch(parameter){
            case "engineTemp":
                if(value > engineTempMax || value < engineTempMin){
                    return 1;
                }
            case "tyresPress":
                if(value > tyresPressMax || value < tyresPressMin){
                    return 1;
                }
            case "oilPress":
                if(value > oilPressMax || value < oilPressMin){
                    return 1;
                }
        }
        return 0;
    }






}
