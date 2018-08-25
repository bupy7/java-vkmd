package ru.mihaly4.vkmd.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.mihaly4.vkmd.R;

public class AboutView extends AbstractView implements IAboutView {
    public AboutView(Stage stage) {
        super(stage);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
    }

    @Override
    protected Parent onCreate() {
        VBox vbox = new VBox(2);
        vbox.setPadding(new Insets(5));
        vbox.setAlignment(Pos.CENTER);

        ImageView logoImgView = new ImageView(new Image(getClass().getResourceAsStream(R.APP_LOGO)));
        logoImgView.setPreserveRatio(true);
        logoImgView.setFitWidth(64);
        vbox.getChildren().add(logoImgView);

        Text appNameTxt = new Text(R.APP_TITLE);
        appNameTxt.setFont(Font.font("sans-serif", FontWeight.EXTRA_BOLD, 13));
        vbox.getChildren().add(appNameTxt);

        Text versionTxt = new Text(R.APP_VERSION);
        vbox.getChildren().add(versionTxt);

        Text copyrightTxt = new Text(String.format("Copyright \u00A9 %s %s", R.APP_YEAR, R.APP_AUTHOR));
        vbox.getChildren().add(copyrightTxt);

        return vbox;
    }

    @Override
    protected void onStart(Parent root) {
        stage.setScene(new Scene(root, 200, 155));
    }
}
