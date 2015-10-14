package com.example.user.mymapfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EditFavourites extends Activity {
    DBHelper db = new DBHelper(this);
    Favourites a = null;
    long ri = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_favourites);
        String id = getIntent().getExtras().getString("id");
        int iid = Integer.parseInt(id);
        ri = (long) iid;

        Button updatebtn = (Button) findViewById(R.id.btnUpdateFavourites);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nametxt = (EditText) findViewById(R.id.editTextName);
                EditText pickuptxt = (EditText) findViewById(R.id.editTextPickup);
                EditText destxt = (EditText) findViewById(R.id.editTextDestination);
                EditText typetxt = (EditText) findViewById(R.id.editTextType);



                db.open();
                db.updateRecord(ri, nametxt.getText().toString(), pickuptxt.getText().toString(), destxt.getText().toString(), typetxt.getText().toString());
                db.close();


                nametxt.setText("");
                pickuptxt.setText("");
                destxt.setText("");
                typetxt.setText("");
                Toast.makeText(EditFavourites.this, "Favourite Updated", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(EditFavourites.this,FavouriteList.class);
                startActivity(intent);
            }
        });



        db.open();
        a = db.getFavourite(iid);
        db.close();

        EditText nametxt = (EditText) findViewById(R.id.editTextName);
        nametxt.setText(a.name);
        EditText pickuptxt = (EditText) findViewById(R.id.editTextPickup);
        pickuptxt.setText(a.pickup);
        EditText destxt = (EditText) findViewById(R.id.editTextDestination);
        destxt.setText(a.destination);
        EditText typetxt = (EditText) findViewById(R.id.editTextType);
        typetxt.setText(a.type);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_favourites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
