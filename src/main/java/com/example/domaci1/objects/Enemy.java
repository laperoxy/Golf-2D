package com.example.domaci1.objects;

import com.example.domaci1.Main;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.geometry.Bounds;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Enemy extends Circle {

    public Enemy(double radius, Translate position, ImagePattern pattern) {

        super(radius);

        super.setFill(pattern);

        super.getTransforms().addAll(
                position
        );

    }

    public boolean handleCollision(Circle ball) {
        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds enemyBounds = super.getBoundsInParent();

        double enemyX = enemyBounds.getCenterX();
        double enemyY = enemyBounds.getCenterY();
        double holeRadius = super.getRadius();

        double distanceX = enemyX - ballX;
        double distanceY = enemyY - ballY;

        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        boolean result = distanceSquared < (holeRadius * holeRadius);

        return result;
    }

}
