module com.slimanice.qlearning {
    requires javafx.controls;
    requires javafx.fxml;
    requires jade;

    opens com.slimanice.qlearning to javafx.fxml;
    exports com.slimanice.qlearning;
    exports com.slimanice.qlearning.sma.agents;
}