package ${packageName};

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Model
public class ${projectName}Model {
    private String data;
    
    public ${projectName}Model(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

        // Controller
        public class ${projectName}Controller {
            private ${projectName}Model model;
            private ${projectName}Activity view;

            public ${projectName}Controller(${projectName}Activity view) {
                this.view = view;
                this.model = new ${projectName}Model("");
            }

            public void updateData(String data) {
                model.setData(data);
                view.displayData(data);
            }
        }

        // View
        public class ${projectName}Activity extends AppCompatActivity {
    private ${projectName}Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new ${projectName}Controller(this);
    }

    public void displayData(String data) {
        // Update UI with data
    }
}