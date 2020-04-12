package application;
	
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;
import ventanas.MainWindow;


public class Main extends Application {
	public static final String APPLICATION_ICON = "/assets/plane.png";
	public static final String SPLASH_IMAGE = "/assets/splash.png";
	    
	private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 600;
    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(SPLASH_IMAGE));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH);
        progressText = new Label("Cargando datos del operador . . .");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                "-fx-background-color: white; " +
                "-fx-border-width:0.5;" 
        );
        splashLayout.setEffect(new DropShadow());
    }
    
    @Override
    public void start(final Stage initStage) throws Exception {
        final Task<ObservableList<String>> friendTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> foundFriends =
                        FXCollections.<String>observableArrayList();
                ObservableList<String> availableFriends =
                        FXCollections.observableArrayList(
                                "Ciudades", "Aeropuertos", "Aerolineas", "Rutas", "Vuelos"
                        );

                updateMessage("Cargando datos del operador . . .");
                for (int i = 0; i < availableFriends.size(); i++) {
                    Thread.sleep(100);
                    updateProgress(i + 1, availableFriends.size());
                    String nextFriend = availableFriends.get(i);
                    foundFriends.add(nextFriend);
                    updateMessage("Cargando datos del operador . . . " + nextFriend);
                }
                Thread.sleep(200);

                return foundFriends;
            }
        };

        showSplash(
                initStage,
                friendTask,
                () -> showMainStage(friendTask.valueProperty())
        );
        new Thread(friendTask).start();
    }

    private void showMainStage(ReadOnlyObjectProperty<ObservableList<String>> friends) {
		try {
			mainStage = new Stage(StageStyle.DECORATED);
	        mainStage.setTitle("Operador de vuelos");
	        mainStage.getIcons().add(new Image(
	                APPLICATION_ICON
	        ));
			MainWindow mw = new MainWindow();
        	mw.show(mainStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        initStage.setScene(splashScene);
        initStage.centerOnScreen();
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }
    
    public interface InitCompletionHandler {
        void complete();
    }
    
    
    
    
    
	public static void main(String[] args) {
		launch(args);
	}
}
