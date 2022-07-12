package com.example.domaci1.objects;

import com.example.domaci1.Main;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Hole extends Circle {

    private int pointsWorth;

    public Hole(double radius, Translate position, int pointsWorth) {
        super(radius);

        this.pointsWorth = pointsWorth;

        Color endColor;

        if (pointsWorth == 5) {
            endColor = Color.GREEN;
        } else if (pointsWorth == 10) {
            endColor = Color.YELLOW;
        } else {
            endColor = Color.ORANGE;
        }

        Stop stops[] = {
                new Stop(0, Color.BLACK),
                new Stop(1, endColor)
        };

        RadialGradient radialGradient = new RadialGradient(
                0, 0,
                0.5, 0.5,
                0.5, true,
                CycleMethod.NO_CYCLE, stops
        );

        super.setFill(radialGradient);

        super.getTransforms().addAll(position);
    }

    public boolean handleCollision(Circle ball) {
        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds holeBounds = super.getBoundsInParent();

        if(ball instanceof Token){
            return ballBounds.intersects(holeBounds);
        }

        double holeX = holeBounds.getCenterX();
        double holeY = holeBounds.getCenterY();
        double holeRadius = super.getRadius();

        double distanceX = holeX - ballX;
        double distanceY = holeY - ballY;

        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        boolean result = distanceSquared < (holeRadius * holeRadius);

        if (ball instanceof Ball) {

            if (result == true && !((Ball) ball).dontUpdate && ((Ball) ball).speed.magnitude() <= Main.MAX_SPEED_FOR_POINT)
                Main.points += this.pointsWorth;

        }


        return result;
    }


    ;
}
