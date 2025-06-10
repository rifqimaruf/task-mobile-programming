package com.example.crud;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<User> userList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int selectedUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton addButton = findViewById(R.id.addButton);
        ListView userListView = findViewById(R.id.userListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        userListView.setAdapter(adapter);

        addButton.setOnClickListener(v -> showInputDialog(false, null));
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedUserId = position;
            showEditDeleteDialog(userList.get(position));
        });

        loadUsers();
    }

    private void showInputDialog(boolean isEdit, User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);

        TextInputEditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        TextInputEditText emailEditText = dialogView.findViewById(R.id.emailEditText);
        MaterialButton btnSave = dialogView.findViewById(R.id.btnSave);

        if (isEdit && user != null) {
            nameEditText.setText(user.getName());
            emailEditText.setText(user.getEmail());
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            if (!name.isEmpty() && !email.isEmpty()) {
                if (isEdit && selectedUserId != -1) {
                    userList.set(selectedUserId, new User(selectedUserId, name, email));
                } else {
                    userList.add(new User(userList.size(), name, email));
                }
                loadUsers();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showEditDeleteDialog(User user) {
        new AlertDialog.Builder(this)
                .setItems(new String[]{"Edit", "Hapus"}, (dialog, which) -> {
                    if (which == 0) {
                        showInputDialog(true, user);
                    } else {
                        userList.remove(selectedUserId);
                        loadUsers();
                        selectedUserId = -1;
                    }
                })
                .show();
    }

    private void loadUsers() {
        adapter.clear();
        for (User user : userList) {
            adapter.add(user.getName() + " (" + user.getEmail() + ")");
        }
        adapter.notifyDataSetChanged();
    }
}