package com.example.domaci1.objects;

import com.example.domaci1.Main;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class Obstacle extends Rectangle {

    private boolean horizontal;

    public Obstacle(double width, double height, Translate position, boolean horizontal) {

        super(width, height, Color.GRAY);

        this.horizontal = horizontal;

        super.getTransforms().addAll(
                position
        );

    }

    public boolean handleCollision(Circle ball) {

        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds obstacleBounds = super.getBoundsInParent();
        double obsX1 = obstacleBounds.getMinX();
        double obsX2 = obstacleBounds.getMaxX();
        double obsY1 = obstacleBounds.getMinY();
        double obsY2 = obstacleBounds.getMaxY();

        if(ball instanceof Token){
            return ballBounds.intersects(obstacleBounds);
        }

        if (ballX + ballRadius < obsX1 || ballX - ballRadius > obsX2)
            return false;

        if (ballY + ballRadius < obsY1 || ballY - ballRadius > obsY2)
            return false;

        if (ball instanceof Ball) {
            Ball myBall = (Ball) ball;

            if (this.horizontal)
                myBall.speed = new Point2D(myBall.speed.getX(), -myBall.speed.getY());
            else
                myBall.speed = new Point2D(-myBall.speed.getX(), myBall.speed.getY());

        }
        return true;

    }

    ;
}