package com.example.andrew.cscb07;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.andrew.cscb07.code.com.users.User;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.database.DatabaseUpdateHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;

import java.util.ArrayList;
import java.util.List;

public class PromoteActivity extends AppCompatActivity {
    private int idInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Button employee = new Button(this);
        boolean alternate = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);
        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeListener(view);
            }
        });

        ConstraintLayout employeeConstraint = (ConstraintLayout) findViewById(R.id.employeeConstraint);
        List<Integer> employees = new ArrayList<>();
        try{
            employees = DatabaseSelectHelpers.getUserIdsByRole(2, this);
        } catch (InvalidIdException e){

        }
        for (Integer employeeId: employees){
            final int myEmployee = employeeId;
            try{
                employee.setText(DatabaseSelectHelpers.getUserDetails(employeeId,this).getName());
            } catch (InvalidIdException e) {
                employee.setText("error");
            }
            employee.setTextColor(Color.parseColor("#fff"));
            employee.setGravity(Gravity.CENTER);
            ConstraintLayout.LayoutParams sizes = new ConstraintLayout.LayoutParams(402, 50);
            employee.setLayoutParams(sizes);
            if (alternate){
                employee.setBackgroundColor(Color.parseColor("#4b4b4b"));
                alternate = false;
            } else if (!alternate){
                employee.setBackgroundColor(Color.parseColor("#3d3d3d"));
                alternate = true;
            }
            employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(PromoteActivity.this);
                    alert.setMessage("Are You Sure?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            try {
                                DatabaseUpdateHelpers.updateUserRole(2, myEmployee, PromoteActivity.this);
                            } catch (InvalidIdException e) {

                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });
                }
            }

    public void homeListener(View view) {
        Intent intent = new Intent(this, StoreActivity.class);
        if (GlobalApplication.roleid == 1){
            intent = new Intent(this, StoreActivity.class);
        } else if (GlobalApplication.roleid == 2){
            intent = new Intent(this, EmployeeActivity.class);
        } else if (GlobalApplication.roleid == 3){
            intent = new Intent(this, AdminActivity.class);
        }
        startActivity(intent);
    }


}
