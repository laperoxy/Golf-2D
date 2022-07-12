package com.example.domaci1;

import com.example.domaci1.objects.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main extends Application implements EventHandler<MouseEvent> {
    private static final double WINDOW_WIDTH = 600;
    private static final double WINDOW_HEIGHT = 800;

    private static final double PLAYER_WIDTH = 20;
    private static final double PLAYER_HEIGHT = 80;
    private static final double PLAYER_MAX_ANGLE_OFFSET = 60;
    private static final double PLAYER_MIN_ANGLE_OFFSET = -60;

    private static final double MS_IN_S = 1e3;
    private static final double NS_IN_S = 1e9;
    private static final double MAXIMUM_HOLD_IN_S = 3;
    public static double MAXIMUM_BALL_SPEED = 1500;
    private static final double BALL_RADIUS = Main.PLAYER_WIDTH / 4;
    private static final double BALL_DAMP_FACTOR = 0.995;
    private static final double MIN_BALL_SPEED = 1;

    public static final double MAX_SPEED_FOR_POINT = 750;

    private static final double HOLE_RADIUS = 3 * BALL_RADIUS;

    public static final double MAX_TIME = 100;
    public static double timePassed = MAX_TIME;

    private double oneSecond;

    public static int points;

    private Group root;
    private Player player;
    private Ball ball;
    private long time;
    private Hole holes[];
    private Obstacle obstacles[];
    private TerrainObstacle terrainObstacles[];
    private Try tries[];
    private Teleporter teleporters[];
    private Enemy enemies[];


    private int numberOfTries;
    private double fenceMes;

    private Rectangle holdAnim;
    static private boolean pressed = false;
    static private double pressedFor = 0;

    static boolean startTimer = false;

    private Rectangle timeLeft;

    private ArrayList<Token> tokens;

    private Image myBackground;

    private boolean gameStarted;

    private PathTransition anim1;
    private PathTransition anim2;

    private void addHoles() {
        Translate hole0Position = new Translate(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.1
        );
        Hole hole0 = new Hole(Main.HOLE_RADIUS, hole0Position, 20);
        this.root.getChildren().addAll(hole0);

        Translate hole1Position = new Translate(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.4
        );
        Hole hole1 = new Hole(Main.HOLE_RADIUS, hole1Position, 5);
        this.root.getChildren().addAll(hole1);

        Translate hole2Position = new Translate(
                Main.WINDOW_WIDTH / 3,
                Main.WINDOW_HEIGHT * 0.25
        );
        Hole hole2 = new Hole(Main.HOLE_RADIUS, hole2Position, 10);
        this.root.getChildren().addAll(hole2);

        Translate hole3Position = new Translate(
                Main.WINDOW_WIDTH * 2 / 3,
                Main.WINDOW_HEIGHT * 0.25
        );
        Hole hole3 = new Hole(Main.HOLE_RADIUS, hole3Position, 10);
        this.root.getChildren().addAll(hole3);

        this.holes = new Hole[]{
                hole0,
                hole1,
                hole2,
                hole3,
        };
    }

    private void addObstacles() {

        final double oppWidth = WINDOW_WIDTH / 40;
        final double oppHeight = WINDOW_HEIGHT / 7;

        Obstacle obs1 = new Obstacle(oppWidth, oppHeight, new Translate(WINDOW_WIDTH / 2 - oppWidth / 2, WINDOW_HEIGHT / 6), false);
        this.root.getChildren().addAll(obs1);

        Obstacle obs2 = new Obstacle(oppHeight, oppWidth, new Translate(WINDOW_WIDTH / 4 - oppWidth, WINDOW_HEIGHT / 2), true);
        this.root.getChildren().addAll(obs2);

        Obstacle obs3 = new Obstacle(oppHeight, oppWidth, new Translate(WINDOW_WIDTH * 3 / 4 - oppHeight + oppWidth, WINDOW_HEIGHT / 2), true);
        this.root.getChildren().addAll(obs3);

        this.obstacles = new Obstacle[]{
                obs1,
                obs2,
                obs3
        };

    }

    private void addTerrainObstacles() {

        final double oppWidth = WINDOW_WIDTH / 20;

        TerrainObstacle mud1 = new TerrainObstacle(oppWidth, new Translate(WINDOW_WIDTH / 6, WINDOW_HEIGHT / 8), true);
        this.root.getChildren().addAll(mud1);

        TerrainObstacle mud2 = new TerrainObstacle(oppWidth, new Translate(WINDOW_WIDTH * 5 / 6 - oppWidth, WINDOW_HEIGHT * 5 / 8), true);
        this.root.getChildren().addAll(mud2);

        TerrainObstacle ice1 = new TerrainObstacle(oppWidth, new Translate(WINDOW_WIDTH * 5 / 6 - oppWidth, WINDOW_HEIGHT / 8), false);
        this.root.getChildren().addAll(ice1);

        TerrainObstacle ice2 = new TerrainObstacle(oppWidth, new Translate(WINDOW_WIDTH / 6, WINDOW_HEIGHT * 5 / 8), false);
        this.root.getChildren().addAll(ice2);


        this.terrainObstacles = new TerrainObstacle[]{
                mud1,
                mud2,
                ice1,
                ice2
        };

    }

    private void addTries() {

        final double tryRad = WINDOW_WIDTH / 120;

        Try try1 = new Try(tryRad, new Translate(WINDOW_WIDTH - 2 * tryRad, 2 * tryRad));
        //this.root.getChildren().addAll(try1);

        Try try2 = new Try(tryRad, new Translate(WINDOW_WIDTH - 5 * tryRad, 2 * tryRad));
        //this.root.getChildren().addAll(try2);

        Try try3 = new Try(tryRad, new Translate(WINDOW_WIDTH - 8 * tryRad, 2 * tryRad));
        //this.root.getChildren().addAll(try3);

        Try try4 = new Try(tryRad, new Translate(WINDOW_WIDTH - 11 * tryRad, 2 * tryRad));
        //this.root.getChildren().addAll(try4);

        Try try5 = new Try(tryRad, new Translate(WINDOW_WIDTH - 14 * tryRad, 2 * tryRad));
        //this.root.getChildren().addAll(try5);

        this.tries = new Try[]{
                try1,
                try2,
                try3,
                try4,
                try5
        };
        this.root.getChildren().addAll(tries);
    }

    private void addToken() {
        final double tokenRad = WINDOW_WIDTH / 70;
        int randomEffect = (int) (Math.random() * 3 + 1);

        double posX = Math.random() * (WINDOW_WIDTH - 2 * fenceMes - tokenRad) + (fenceMes + tokenRad);
        double posY = Math.random() * (WINDOW_HEIGHT - 2 * fenceMes - tokenRad) + (fenceMes + tokenRad);

        Translate pos = new Translate(posX, posY);
        Token token = new Token(tokenRad, pos, randomEffect);

        boolean overlapsObs = Arrays.stream(this.obstacles).anyMatch(obstacle -> obstacle.handleCollision(token));
        boolean overlapsHole = Arrays.stream(this.holes).anyMatch(hole -> hole.handleCollision(token));
        boolean overlapTeleporters = Arrays.stream(this.teleporters).anyMatch(teleporter -> teleporter.handleCollision(token));

        if (!overlapsObs && !overlapsHole && !overlapTeleporters) {
            tokens.add(token);
            this.root.getChildren().addAll(token);
        }

    }

    private void addTeleporters() {
        final double teleporterRad = WINDOW_WIDTH / 40;

        Teleporter teleporter1 = new Teleporter(teleporterRad, new Translate(WINDOW_WIDTH / 3, WINDOW_HEIGHT * 2.8 / 5));

        Teleporter teleporter2 = new Teleporter(teleporterRad, new Translate(WINDOW_WIDTH * 2 / 3, WINDOW_HEIGHT * 2.3 / 5));

        teleporter1.setMyPair(teleporter2);
        teleporter2.setMyPair(teleporter1);

        this.root.getChildren().addAll(teleporter1, teleporter2);

        this.teleporters = new Teleporter[]{
                teleporter1,
                teleporter2
        };

    }

    private void addEnemies() {
        final double enemyRad = WINDOW_WIDTH / 30;
        double posX = -WINDOW_WIDTH;
        double posY1 = WINDOW_HEIGHT * 2 / 3;

        double posY2 = WINDOW_HEIGHT / 2;


        Image enemyPattern = new Image(Main.class.getClassLoader().getResourceAsStream("enemy.png"));
        ImagePattern enemyImage = new ImagePattern(enemyPattern);

        Path path1 = new Path(
                new MoveTo(0, 0),
                new LineTo(3 * WINDOW_WIDTH, 0),
                new VLineTo(WINDOW_HEIGHT),
                new LineTo(0, WINDOW_HEIGHT),
                new ClosePath()
        );

        Path path2 = new Path(
                new MoveTo(0, 0),
                new LineTo(WINDOW_WIDTH + WINDOW_WIDTH / 2, -WINDOW_HEIGHT / 2 + enemyRad + fenceMes),
                new LineTo(3 * WINDOW_WIDTH, 0),
                new ClosePath()
        );

        Enemy enemy1 = new Enemy(enemyRad, new Translate(posX, posY1), enemyImage);

        Enemy enemy2 = new Enemy(enemyRad, new Translate(posX, posY2), enemyImage);

        anim1 = new PathTransition();
        anim1.setDuration(Duration.seconds(40));
        anim1.setNode(enemy1);
        anim1.setPath(path1);
        anim1.setCycleCount(Animation.INDEFINITE);
        //anim1.play();

        anim2 = new PathTransition();
        anim2.setDuration(Duration.seconds(40));
        anim2.setNode(enemy2);
        anim2.setPath(path2);
        anim2.setCycleCount(Animation.INDEFINITE);
        //anim2.play();


        this.root.getChildren().addAll(enemy1, enemy2);

        this.enemies = new Enemy[]{
                enemy1,
                enemy2
        };

    }

    @Override
    public void start(Stage stage) throws IOException {
        this.root = new Group();

        this.gameStarted = false;
        //MENI

        Text text = new Text("GOLF 2D");
        text.setFont(Font.font("verdana", 30));
        text.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2 - WINDOW_WIDTH / 10, WINDOW_HEIGHT / 7)
        );
        text.setFill(Color.WHITE);

        Button trava = new Button("Golf na travi");
        trava.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2 - WINDOW_WIDTH / 15, WINDOW_HEIGHT / 4)
        );

        Button pesak = new Button("Golf na pesku");
        pesak.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2 - WINDOW_WIDTH / 15, WINDOW_HEIGHT / 2)
        );

        Button beton = new Button("Golf na betonu");
        beton.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2 - WINDOW_WIDTH / 15, WINDOW_HEIGHT * 3 / 4)
        );

        Group izborTerena = new Group();
        izborTerena.getChildren().addAll(trava, pesak, beton, text);

        Image menuPattern = new Image(Main.class.getClassLoader().getResourceAsStream("menu.jpg"));
        ImagePattern menuBackground = new ImagePattern(menuPattern);

        Scene mainMenu = new Scene(izborTerena, Main.WINDOW_WIDTH, WINDOW_HEIGHT, menuBackground);

        stage.setScene(mainMenu);

        //

        //TOPOVI

        Text textTopovi = new Text("IZBOR TOPA");
        textTopovi.setFont(Font.font("verdana", 30));
        textTopovi.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2 - WINDOW_WIDTH / 10, WINDOW_HEIGHT / 7)
        );
        textTopovi.setFill(Color.WHITE);


        Button spor = new Button("Spori top");
        spor.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2 - WINDOW_WIDTH / 15, WINDOW_HEIGHT / 4)
        );

        Button srednji = new Button("Top srednje brzine");
        srednji.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2 - WINDOW_WIDTH / 15, WINDOW_HEIGHT / 2)
        );

        Button brz = new Button("Brzi top");
        brz.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2 - WINDOW_WIDTH / 15, WINDOW_HEIGHT * 3 / 4)
        );

        Group izborTopa = new Group();
        izborTopa.getChildren().addAll(spor, srednji, brz, textTopovi);

        Scene canonMenu = new Scene(izborTopa, Main.WINDOW_WIDTH, WINDOW_HEIGHT, Color.BLACK);

        //


        //Pozadina

        Image backgroundPattern = new Image(Main.class.getClassLoader().getResourceAsStream("grass.jpg"));
        ImagePattern background = new ImagePattern(backgroundPattern);

        //


        this.numberOfTries = 4;
        tokens = new ArrayList<>();
        oneSecond = 0;


        //Ograda

        Image fenceBackground = new Image(Main.class.getClassLoader().getResourceAsStream("fence.jpg"));
        ImagePattern fencePattern = new ImagePattern(fenceBackground);

        fenceMes = WINDOW_WIDTH / 30;

        Rectangle fenceTop = new Rectangle(WINDOW_WIDTH, fenceMes);
        fenceTop.setFill(fencePattern);

        Rectangle fenceBottom = new Rectangle(WINDOW_WIDTH, fenceMes);
        fenceBottom.getTransforms().addAll(
                new Translate(0, WINDOW_HEIGHT - fenceMes)
        );
        fenceBottom.setFill(fencePattern);

        Rectangle fenceLeft = new Rectangle(fenceMes, WINDOW_HEIGHT);
        fenceLeft.setFill(fencePattern);

        Rectangle fenceRight = new Rectangle(fenceMes, WINDOW_HEIGHT);
        fenceRight.getTransforms().addAll(
                new Translate(WINDOW_WIDTH - fenceMes, 0)
        );
        fenceRight.setFill(fencePattern);

        this.root.getChildren().addAll(fenceTop, fenceBottom, fenceLeft, fenceRight);

        //


        //Indikator Brzine
        this.holdAnim = new Rectangle(WINDOW_WIDTH / 50, 0);
        Scale scale = new Scale();
        this.holdAnim.getTransforms().addAll(
                scale
        );
        this.holdAnim.setFill(Color.RED);
        this.root.getChildren().addAll(this.holdAnim);
        //


        //Poeni

        this.points = 0;
        Text pointsText = new Text("" + this.points);
        pointsText.setFill(Color.BLACK);
        pointsText.setStyle("-fx-font:24 ariel;");
        pointsText.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 60, WINDOW_HEIGHT / 40)
        );
        this.root.getChildren().addAll(pointsText);

        //

        //Vreme
        timeLeft = new Rectangle(WINDOW_WIDTH / 30, WINDOW_HEIGHT);
        timeLeft.getTransforms().addAll(
                new Translate(WINDOW_WIDTH - WINDOW_WIDTH / 30, 0)
        );
        timeLeft.setFill(Color.GREEN);
        this.root.getChildren().addAll(timeLeft);

        Text timeText = new Text("" + (int) timePassed);
        timeText.setFill(Color.WHITE);
        timeText.setStyle("-fx-font:12 ariel;");
        timeText.getTransforms().addAll(
                new Translate(WINDOW_WIDTH - WINDOW_WIDTH / 37, WINDOW_HEIGHT / 20)
        );
        this.root.getChildren().addAll(timeText);
        //


        Scene scene = new Scene(this.root, Main.WINDOW_WIDTH, WINDOW_HEIGHT, background);

        trava.setOnAction(actionEvent -> {
            myBackground = new Image(Main.class.getClassLoader().getResourceAsStream("grass.jpg"));
            ImagePattern backgroundImage = new ImagePattern(myBackground);
            scene.setFill(backgroundImage);
            stage.setScene(canonMenu);
        });

        pesak.setOnAction(actionEvent -> {
            myBackground = new Image(Main.class.getClassLoader().getResourceAsStream("sand.jpg"));
            ImagePattern backgroundImage = new ImagePattern(myBackground);
            scene.setFill(backgroundImage);
            stage.setScene(canonMenu);
        });

        beton.setOnAction(actionEvent -> {
            myBackground = new Image(Main.class.getClassLoader().getResourceAsStream("concrete.jpg"));
            ImagePattern backgroundImage = new ImagePattern(myBackground);
            scene.setFill(backgroundImage);
            stage.setScene(canonMenu);
        });


        Translate playerPosition = new Translate(
                Main.WINDOW_WIDTH / 2 - Main.PLAYER_WIDTH / 2,
                Main.WINDOW_HEIGHT - Main.PLAYER_HEIGHT
        );

        this.player = new Player(
                Main.PLAYER_WIDTH,
                Main.PLAYER_HEIGHT,
                playerPosition,
                "srednji"
        );

        spor.setOnAction(actionEvent -> {
            this.player = new Player(
                    Main.PLAYER_WIDTH,
                    Main.PLAYER_HEIGHT,
                    playerPosition,
                    "spor"
            );
            stage.setScene(scene);
            this.root.getChildren().addAll(this.player);
        });

        srednji.setOnAction(actionEvent -> {
            this.player = new Player(
                    Main.PLAYER_WIDTH,
                    Main.PLAYER_HEIGHT,
                    playerPosition,
                    "srednji"
            );
            stage.setScene(scene);
            this.root.getChildren().addAll(this.player);
        });

        brz.setOnAction(actionEvent -> {
            this.player = new Player(
                    Main.PLAYER_WIDTH,
                    Main.PLAYER_HEIGHT,
                    playerPosition,
                    "brz"
            );
            stage.setScene(scene);
            this.root.getChildren().addAll(this.player);
        });


        this.addHoles();
        this.addObstacles();
        this.addTerrainObstacles();
        this.addTries();
        this.addTeleporters();
        this.addEnemies();


        scene.addEventHandler(
                MouseEvent.MOUSE_MOVED,
                mouseEvent -> this.player.handleMouseMoved(
                        mouseEvent,
                        Main.PLAYER_MIN_ANGLE_OFFSET,
                        Main.PLAYER_MAX_ANGLE_OFFSET
                )
        );

        scene.addEventHandler(MouseEvent.ANY, this);

        scene.addEventHandler(KeyEvent.KEY_PRESSED,
                keyEvent -> handleKeyEvent(keyEvent)
        );


        Timer timer = new Timer(
                deltaNanoseconds -> {

                    double deltaSeconds = (double) deltaNanoseconds / Main.NS_IN_S;

                    if (gameStarted) {
                        anim1.play();
                        anim2.play();
                        oneSecond += deltaSeconds;
                        if (oneSecond >= 1) {
                            oneSecond = 0;
                            int random = (int) (Math.random() * 100 + 1);
                            if (random < 50)
                                addToken();
                        }

                        if (tokens.size() > 0) {
                            for (int i = 0; i < tokens.size(); ++i) {
                                boolean isteklo = tokens.get(i).istekloVreme(deltaSeconds);
                                if (isteklo) {
                                    this.root.getChildren().remove(tokens.get(i));
                                    tokens.remove(i);
                                }
                            }
                        }
                    }


                    if (startTimer) {
                        timePassed -= deltaSeconds;
                        timeText.setText("" + (int) timePassed);
                        timeText.setTranslateY(WINDOW_HEIGHT - timeLeft.getHeight());
                    }
                    if (timePassed <= 0) stage.close();
                    timeLeft.setHeight((timePassed / MAX_TIME) * WINDOW_HEIGHT);
                    timeLeft.setTranslateY(WINDOW_HEIGHT - timeLeft.getHeight());
                    if (pressed) {
                        if (pressedFor <= 3)
                            pressedFor += deltaSeconds;
                        holdAnim.setHeight(pressedFor / 3 * WINDOW_HEIGHT);
                        holdAnim.setTranslateY(WINDOW_HEIGHT - holdAnim.getHeight());
                    } else {
                        pressedFor = 0;
                        holdAnim.setHeight(0);
                    }
                    if (this.ball != null) {
                        boolean stopped = this.ball.update(
                                deltaSeconds,
                                fenceMes,
                                Main.WINDOW_WIDTH - fenceMes,
                                fenceMes,
                                Main.WINDOW_HEIGHT - fenceMes,
                                Main.BALL_DAMP_FACTOR,
                                Main.MIN_BALL_SPEED
                        );

                        this.root.getChildren().remove(this.tries[numberOfTries]);
                        if (this.numberOfTries < 0) {
                            stage.close();
                        }

                        if (tokens.size() > 0) {
                            for (int i = 0; i < tokens.size(); ++i) {
                                boolean hit = tokens.get(i).handleCollision(this.ball);
                                if (hit) {
                                    if (tokens.get(i).tokenEffect == 1) {
                                        Main.points++;
                                        pointsText.setText("" + this.points);
                                    } else if (tokens.get(i).tokenEffect == 2) {


                                    } else if (tokens.get(i).tokenEffect == 3) {
                                        if (Main.timePassed + 30 <= Main.MAX_TIME)
                                            Main.timePassed += 30;
                                        else
                                            Main.timePassed = Main.MAX_TIME;
                                    }
                                    this.root.getChildren().remove(tokens.get(i));
                                    tokens.remove(i);
                                }
                            }
                        }

                        Arrays.stream(this.obstacles).anyMatch(obstacle -> obstacle.handleCollision(this.ball));
                        Arrays.stream(this.terrainObstacles).anyMatch(terrainObstacle -> terrainObstacle.handleCollision(this.ball));
                        Arrays.stream(this.teleporters).anyMatch(teleporter -> teleporter.handleCollision(this.ball));

                        boolean isInHole = Arrays.stream(this.holes).anyMatch(hole -> hole.handleCollision(this.ball));

                        boolean hitEnemy = Arrays.stream(this.enemies).anyMatch(enemy -> enemy.handleCollision(this.ball));

                        if (isInHole && this.ball.speed.magnitude() <= MAX_SPEED_FOR_POINT && !this.ball.dontUpdate) {
                            this.ball.dontUpdate = true;
                            pointsText.setText("" + this.points);

                            Timeline timeline = new Timeline(
                                    new KeyFrame(Duration.ZERO,
                                            new KeyValue(this.ball.scale.xProperty(), 1, Interpolator.LINEAR),
                                            new KeyValue(this.ball.scale.yProperty(), 1, Interpolator.LINEAR)),
                                    new KeyFrame(Duration.seconds(2),
                                            new KeyValue(this.ball.scale.xProperty(), 0, Interpolator.LINEAR),
                                            new KeyValue(this.ball.scale.yProperty(), 0, Interpolator.LINEAR))
                            );
                            timeline.play();
                            timeline.setOnFinished(actionEvent -> {
                                this.root.getChildren().remove(this.ball);
                                this.ball = null;
                                this.numberOfTries--;
                                if (this.numberOfTries < 0) {
                                    stage.close();
                                }
                            });
                        }

                        if ((stopped || hitEnemy) && !isInHole) {
                            this.root.getChildren().remove(this.ball);
                            this.ball = null;
                            this.numberOfTries--;
                            if (this.numberOfTries < 0) {
                                stage.close();
                            }
                        }

                    }
                }
        );
        timer.start();

        scene.setCursor(Cursor.NONE);

        stage.setTitle("Golfer");
        stage.setResizable(false);
        //stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED) && mouseEvent.isPrimaryButtonDown()) {
            this.time = System.currentTimeMillis();
            pressed = true;
        } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            gameStarted = true;
            pressed = false;
            startTimer = true;
            if (this.time != -1 && this.ball == null) {
                double value = (System.currentTimeMillis() - this.time) / Main.MS_IN_S;
                double deltaSeconds = Utilities.clamp(value, 0, Main.MAXIMUM_HOLD_IN_S);

                double myStartingSpeed;

                if (this.player.tip.equals("spor"))
                    myStartingSpeed = Main.MAXIMUM_BALL_SPEED - 500;
                else if (this.player.tip.equals("srednji"))
                    myStartingSpeed = Main.MAXIMUM_BALL_SPEED;
                else
                    myStartingSpeed = Main.MAXIMUM_BALL_SPEED + 500;

                double ballSpeedFactor = deltaSeconds / Main.MAXIMUM_HOLD_IN_S * myStartingSpeed;

                Translate ballPosition = this.player.getBallPosition();
                Point2D ballSpeed = this.player.getSpeed().multiply(ballSpeedFactor);

                this.ball = new Ball(Main.BALL_RADIUS, ballPosition, ballSpeed);
                this.root.getChildren().addAll(this.ball);
            }
            this.time = -1;
        }
    }

    public void handleKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED) && keyEvent.getCode() == KeyCode.SPACE) {
            this.root.getChildren().remove(this.ball);
            this.ball = null;
            this.numberOfTries--;
            if (this.numberOfTries < 0) {
                Platform.exit();
            }
        }
    }

}