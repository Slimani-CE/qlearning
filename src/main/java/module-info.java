module com.slimanice.qlearning {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.slimanice.qlearning to javafx.fxml;
    exports com.slimanice.qlearning;
}