package com.example.domaci1.objects;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class TerrainObstacle extends Rectangle {

    private boolean isMud;

    public TerrainObstacle(double width, Translate position, boolean isMud) {

        super(width, width);

        this.isMud = isMud;

        if (this.isMud)
            super.setFill(Color.BROWN);
        else
            super.setFill(Color.LIGHTBLUE);

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

        if (ballX + ballRadius < obsX1 || ballX - ballRadius > obsX2)
            return false;

        if (ballY + ballRadius < obsY1 || ballY - ballRadius > obsY2)
            return false;


        Ball myBall = (Ball) ball;

        if (isMud)
            myBall.speed = new Point2D(myBall.speed.getX() * 0.97, myBall.speed.getY() * 0.97);
        else
            myBall.speed = new Point2D(myBall.speed.getX() * 1.03, myBall.speed.getY() * 1.03);


        return true;

    }

}
