package com.example.domaci1.objects;

import com.example.domaci1.Main;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class Teleporter extends Circle {

    Teleporter myPair;

    public Teleporter(double radius, Translate position) {

        super(radius);

        Stop stops[] = {
                new Stop(0, Color.PURPLE),
                new Stop(1, Color.BLACK)
        };

        RadialGradient radialGradient = new RadialGradient(
                0, 0,
                0.5, 0.5,
                0.5, true,
                CycleMethod.NO_CYCLE, stops
        );

        super.setFill(radialGradient);

        super.getTransforms().addAll(
                position
        );

    }

    public void setMyPair(Teleporter pair) {
        this.myPair = pair;
    }

    public boolean handleCollision(Circle ball) {
        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds teleporterBounds = super.getBoundsInParent();

        Bounds holeBounds = super.getBoundsInParent();

        if(ball instanceof Token){
            return ballBounds.intersects(teleporterBounds);
        }

        double telX = teleporterBounds.getCenterX();
        double telY = teleporterBounds.getCenterY();
        double telRadius = super.getRadius();

        double distanceX = telX - ballX;
        double distanceY = telY - ballY;

        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        boolean result = distanceSquared < (telRadius * telRadius);

        double newPosX;
        double newPosY;

        Bounds pairBounds = myPair.getBoundsInParent();

        if (result) {
            if (ball instanceof Ball) {
                double speed = ((Ball) ball).speed.getY();
                if (speed < 0) {
                    newPosY = pairBounds.getCenterY() - 2 * telRadius;
                    newPosX = pairBounds.getCenterX();
                } else {
                    newPosY = pairBounds.getCenterY() + 2 * telRadius;
                    newPosX = pairBounds.getCenterX();
                }
                ((Ball) ball).position.setX(newPosX);
                ((Ball) ball).position.setY(newPosY);
            }
        }


        return result;
    }

}
