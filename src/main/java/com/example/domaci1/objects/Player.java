package com.example.domaci1.objects;

import com.example.domaci1.Main;
import com.example.domaci1.Utilities;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Player extends Group {

    private double width;
    private double height;
    private Translate position;
    private Rotate rotate;
    public String tip;

    public Player(double width, double height, Translate position, String tip) {
        this.width = width;
        this.height = height;
        this.position = position;
        this.tip = tip;

        final double headRad = width / 2;
        Circle head = new Circle(headRad);
        head.getTransforms().addAll(
                new Translate(width / 2, height - headRad)
        );
        head.setFill(Color.ORANGE);

        Path cannon = new Path();

        cannon = getCannonShape(width, height);

        cannon.getTransforms().addAll(
                new Translate(0, -headRad)
        );

        if (this.tip.equals("spor")) {
            cannon.setFill(Color.LIGHTBLUE);
            Main.MAXIMUM_BALL_SPEED = 1000;
        } else if (this.tip.equals("srednji")) {
            cannon.setFill(Color.DARKBLUE);
            Main.MAXIMUM_BALL_SPEED = 1500;
        } else {
            cannon.setFill(Color.DARKRED);
            Main.MAXIMUM_BALL_SPEED = 2000;
        }


        super.getChildren().addAll(cannon, head);

        this.rotate = new Rotate();

        super.getTransforms().addAll(
                position,
                new Translate(width / 2, height - width / 2),
                rotate,
                new Translate(-width / 2, -(height - width / 2))
        );
    }

    private Path getCannonShape(double width, double height) {
        Path cannon;
        if (this.tip.equals("spor")) {

            cannon = new Path(
                    new MoveTo(width / 4, 0),
                    new LineTo(0, height),
                    new HLineTo(width),
                    new LineTo(3 * width / 4, 0),
                    new ClosePath()
            );

        } else if (this.tip.equals("srednji")) {

            cannon = new Path(
                    new MoveTo(0, 0),
                    new LineTo(0, height),
                    new HLineTo(width),
                    new LineTo(width, 0),
                    new ClosePath()
            );

        } else {
            cannon = new Path(
                    new MoveTo(0, 0),
                    new LineTo(width /3, height),
                    new HLineTo(width *2/3),
                    new LineTo(width, 0),
                    new ClosePath()
            );
        }
        return cannon;
    }

    public void handleMouseMoved(MouseEvent mouseEvent, double minAngleOffset, double maxAngleOffset) {
        Bounds bounds = super.getBoundsInParent();

        double startX = bounds.getCenterX();
        double startY = bounds.getMaxY();

        double endX = mouseEvent.getX();
        double endY = mouseEvent.getY();

        Point2D direction = new Point2D(endX - startX, endY - startY).normalize();
        Point2D startPosition = new Point2D(0, -1);

        double angle = (endX > startX ? 1 : -1) * direction.angle(startPosition);

        this.rotate.setAngle(Utilities.clamp(angle, minAngleOffset, maxAngleOffset));
    }

    public Translate getBallPosition() {
        double startX = this.position.getX() + this.width / 2;
        double startY = this.position.getY() + this.height - width / 2;

        double x = startX + Math.sin(Math.toRadians(this.rotate.getAngle())) * this.height;
        double y = startY - Math.cos(Math.toRadians(this.rotate.getAngle())) * this.height;

        Translate result = new Translate(x, y);

        return result;
    }

    public Point2D getSpeed() {
        double startX = this.position.getX() + this.width / 2;
        double startY = this.position.getY() + this.height - width / 2;

        double endX = startX + Math.sin(Math.toRadians(this.rotate.getAngle())) * this.height;
        double endY = startY - Math.cos(Math.toRadians(this.rotate.getAngle())) * this.height;

        Point2D result = new Point2D(endX - startX, endY - startY);

        return result.normalize();
    }
}
