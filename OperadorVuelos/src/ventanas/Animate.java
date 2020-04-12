package ventanas;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Animate {

	
	public static void animateAnchorPane(AnchorPane a) {
		FadeTransition t = new FadeTransition(Duration.millis(200),a);
		t.setFromValue(0);
		t.setToValue(100);
		t.setCycleCount(1);
		t.setInterpolator(Interpolator.LINEAR);
		t.play();
	}
	
}