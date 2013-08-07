/**
 * Your application code goes here
 */

package userclasses;

import generated.StateMachineBase;
import com.codename1.ui.*; 
import com.codename1.ui.events.*;
import com.codename1.ui.util.Resources;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;
import com.codename1.components.InfiniteProgress;


/**
 *
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }
    
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
	protected void initVars(Resources res){
	}

    @Override
    protected void beforeMain(Form f) {
    	updateContent();
    }
    
    private void updateContent() {
        //InfiniteProgress inf = new InfiniteProgress();
        //Dialog progress = inf.showInifiniteBlocking();
        //updateContent(progress);
    	updateContentImpl();
    }
    
    //private void updateContent(final Dialog progress) {
    private void updateContentImpl() {    	
        try {
            ConnectionRequest req = new ConnectionRequest() {
                protected void readResponse(InputStream input) throws IOException {
                	try{
                    JSONParser p = new JSONParser();
                    Hashtable h = p.parse(new InputStreamReader(input));
                    
                    //// "status" : "REQUEST_DENIED"
                    //String response = (String)h.get("status");
                    //if(response.equals("REQUEST_DENIED")){
                        //System.out.println("Something wrong");
                        //progress.dispose();
                        //Dialog.show("Info", "Request denied", "Ok", null);
                        //return;
                    //}
                        
                    final Vector v = (Vector) h.get("root");
                    for (int i = 0; i < v.size(); i++) {
                        Hashtable entry = (Hashtable) v.elementAt(i);
                        String weekday = (String) entry.get("weekday");
                        String weekday_chinese = (String) entry.get("weekday_chinese");
                        String start_time = (String) entry.get("start_time");
                        String end_time = (String) entry.get("end_time");
                        String lesson = (String) entry.get("lesson");
                        String lesson_chinese = (String) entry.get("lesson_chinese");
                        String site = (String) entry.get("site");
                        String site_address = (String) entry.get("site_address");
                        String teacher = (String) entry.get("teacher");         
                        findList().getModel().addItem(weekday + 
                        		"," + weekday_chinese +
                        		"," + start_time +
                        		"," + end_time +
                        		"," + lesson +
                        		"," + lesson_chinese +
                        		"," + site +
                        		"," + site_address +
                        		"," + teacher
                        		);
                    }
                    //progress.dispose();
                	}
                	catch(IOException ex) {
                		ex.printStackTrace();
                	}
                }
            };
            //req.setUrl("http://54.213.75.56");
            //req.setUrl("http://localhost:8000");
            req.setUrl("http://timetable.sinaapp.com");
            req.setPost(false);

            NetworkManager.getInstance().addToQueue(req);
        } //https://maps.googleapis.com/maps/api/place/search/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&sensor=false&key=AIzaSyDdCsmiS9AT6MfFEWi_Vy87LJ0B2khZJME
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
