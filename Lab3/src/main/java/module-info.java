module haifauni.imree.lab3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens haifauni.imree.lab3 to javafx.fxml;
    exports haifauni.imree.lab3;
}