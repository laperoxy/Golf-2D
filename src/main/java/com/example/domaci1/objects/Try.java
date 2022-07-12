package com.example.domaci1.objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class Try extends Circle {

    public Try(double radius, Translate position) {

        super(radius,Color.RED);
        super.getTransforms().addAll(position);

    }

}
