/**
 * Your application code goes here
 */

package userclasses;

import generated.StateMachineBase;
import com.codename1.ui.*; 
import com.codename1.ui.events.*;
import com.codename1.ui.layouts.CoordinateLayout;
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
import com.codename1.ui.geom.Rectangle;;


/**
 *
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
	private Form mainform;
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
    protected void postMain(Form f) {
    	Label l = findLabel();
    	int display_w = Display.getInstance().getDisplayWidth();
    	int display_h = Display.getInstance().getDisplayHeight();
    	f.setLayout(new CoordinateLayout(display_w, display_h));
    	l.setX(0);
    	l.setY(0);
    	mainform = f;
    	updateContent();
    }
    
    private void updateContent() {
        InfiniteProgress inf = new InfiniteProgress();
        Dialog progress = inf.showInifiniteBlocking();
        updateContent(progress);
    }
    
    private int getWeekDay(String weekday)
    {
    	if (weekday.equalsIgnoreCase("Monday"))
    		return 0;
    	if (weekday.equalsIgnoreCase("Tuesday"))
    		return 1;
    	if (weekday.equalsIgnoreCase("Wednesday"))
    		return 2;
    	if (weekday.equalsIgnoreCase("Thursday"))
    		return 3;
    	if (weekday.equalsIgnoreCase("Friday"))
    		return 4;
    	if (weekday.equalsIgnoreCase("Saturday"))
    		return 5;
    	if (weekday.equalsIgnoreCase("Sunday"))
    		return 6;
    	return 0;
    }
    private double getRow(String start_time, String end_time)
    {
    	return 4;
    }
    
    private Rectangle calculatePosition(String weekday, String start_time, String end_time)
    {
    	int w=1072;
    	int h=750;
    	int t=176;
    	int l=125;
    	int cellw=118;
    	int sepw=16;
    	int cellh=46;
    	int seph=0;
    	int cols=6;
    	int rows=9;
    	
    	int x = l + getWeekDay(weekday) * (cellw + sepw);
    	int y = (int) (t + getRow(start_time, end_time) * (cellh + seph));
    	
    	Rectangle result = new Rectangle(x, y, cellw, cellh);
    	return result;
    }
    
    private Rectangle convertToDisplay(Rectangle rc)
    {
    	int w=1072;
    	int h=750;
    	
    	int display_w = Display.getInstance().getDisplayWidth();
    	int display_h = Display.getInstance().getDisplayHeight() + 20;
    	
    	int x = (int) (rc.getX() / (double)h * display_h);
    	int y = (int) (rc.getY() / (double)h * display_h);
    	int h1 = (int) (rc.getSize().getWidth() / (double)h * display_h);
    	int w1 = (int) (rc.getSize().getHeight() / (double)h * display_h);
    	return new Rectangle(x, y, h1, w1);
    }
    
    private void updateContent(final Dialog progress) {
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
//                        findList().getModel().addItem(weekday + 
//                        		"," + weekday_chinese +
//                        		"," + start_time +
//                        		"," + end_time +
//                        		"," + lesson +
//                        		"," + lesson_chinese +
//                        		"," + site +
//                        		"," + site_address +
//                        		"," + teacher
//                        		);
                        Label l = new Label(lesson);
                        Rectangle rc = convertToDisplay(calculatePosition(weekday, start_time, end_time)); 
                    	l.setX(rc.getX());
                    	l.setY(rc.getY());
                    	l.setPreferredSize(rc.getSize());
//                    	l.getUnselectedStyle().setBgTransparency(100);
//                    	l.getUnselectedStyle().setBgColor(0xff);
                    	mainform.addComponent(l);

                    }
                    progress.dispose();
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
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
