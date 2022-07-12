package com.example.domaci1.objects;

import com.example.domaci1.Main;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class Token extends Circle {

    public int tokenEffect;
    private double vremeTrajanja;

    public Token(double radius, Translate position, int effect){
        super(radius);

        this.tokenEffect=effect;
        Color tokenColor=Color.BLACK;

        vremeTrajanja=Math.random()*8 + 5;

        if(tokenEffect==1){
            tokenColor=Color.YELLOW;
        }else if(tokenEffect==2){
            tokenColor=Color.DARKRED;
        }else if(tokenEffect==3){
            tokenColor=Color.LIME;
        }

        super.setFill(tokenColor);

        super.getTransforms().addAll(position);

    }

    public boolean handleCollision(Circle ball) {
        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds holeBounds = super.getBoundsInParent();

        double tokenX = holeBounds.getCenterX();
        double tokenY = holeBounds.getCenterY();
        double tokenRad = super.getRadius();

        double distanceX = tokenX - ballX;
        double distanceY = tokenY - ballY;

        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        boolean result = ballBounds.intersects(holeBounds);

        return result;
    }

    public boolean istekloVreme(double isteklo){
        if(vremeTrajanja<=0)
            return true;

        vremeTrajanja-=isteklo;
        return false;
    }

}
