/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    TextView ghostText;
    TextView gameStatus;
    //SimpleDictionary simpleDictionary;
    FastDictionary simpleDictionary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ghostText=(TextView)findViewById(R.id.ghostText);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();

        try {
            //simpleDictionary=new SimpleDictionary(assetManager.open("words.txt"));
            simpleDictionary=new FastDictionary(assetManager.open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn("");
        }
        return true;
    }

    private void computerTurn(String fragment) {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        ghostText=(TextView)findViewById(R.id.ghostText);

        if(fragment.length()>=4&&simpleDictionary.isWord(fragment))
        {
            label.setText("Computer Won");
        }
        else{
            String word=simpleDictionary.getAnyWordStartingWith(fragment);
            if(word==null)
            {
                label.setText("Computer won");
            }
            else{
                ghostText.append(word.charAt(fragment.length())+"");
            }
        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }
    public void challenge(View view){
        ghostText=(TextView)findViewById(R.id.ghostText);
        gameStatus=(TextView)findViewById(R.id.gameStatus);
        if(ghostText.getText().toString().length()>=4&&simpleDictionary.isWord(ghostText.getText().toString())||
                simpleDictionary.getAnyWordStartingWith(ghostText.getText().toString())==null)
        {
            if(simpleDictionary.getAnyWordStartingWith(ghostText.getText().toString())==null)
            {
                gameStatus.setText("USER WINS"+"\n"+"NO WORD CAN BE FORMED FROM THIS PREFIX");
            }
            else
            gameStatus.setText("USER WINS"+"\n"+"WORD FOUND IN DICTIONARY");

        }
        else
        {
            gameStatus.setText("Computer Wins");
        }
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        keyCode=event.getUnicodeChar();
        gameStatus=(TextView)findViewById(R.id.gameStatus);
        ghostText=(TextView)findViewById(R.id.ghostText);
        if(keyCode>='a'&&keyCode<='z'||keyCode>='A'&&keyCode<='Z')
        {
            if(ghostText.getText().toString().length()>=4&&simpleDictionary.isWord(ghostText.getText().toString()))
            {
                gameStatus.setText("Computer wins");
                return false;
            }
            ghostText.append((char)keyCode+"");
            gameStatus.setText(COMPUTER_TURN);
            userTurn=false;
            computerTurn(ghostText.getText().toString());
            return true;
        }
        else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
