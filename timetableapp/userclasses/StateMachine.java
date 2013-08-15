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
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.Dialog;


/**
 *
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
	private Form mainform;
	private int image_h;
	
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
    	if (f.getName() != "Main")
    		return;
    	mainform = f;
    	Label l = findLabel();
    	image_h = l.getIcon().getHeight();
    	int display_w = Display.getInstance().getDisplayWidth();
    	int display_h = Display.getInstance().getDisplayHeight();
    	f.setLayout(new CoordinateLayout(display_w, display_h));
    	l.setX(0);
    	l.setY(0);
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
    
    private double getRow(String time)
    {
    	int index = time.indexOf(":");
    	if (index == -1)
    		return 0;
    	int s_h = Integer.parseInt(time.substring(0, index));
    	
    	double result = 0;
    	String min_sec = time.substring(index + 1);
    	int index2 = min_sec.indexOf(":");
    	if (index2 == -1)
    		result = s_h;
    	else {
    		int s_m = Integer.parseInt(min_sec.substring(0, index2));
    		result = s_h + s_m / 60.0;
    	}
    	return (result - 9) / 13.0 /*hours*/ * 13.0 /*rows*/; // 13 rows present 13 hours starting from 9:00AM
    }
    
    private Rectangle calculatePosition(String weekday, String start_time, String end_time)
    {
    	int w=1235;
    	int h=1000;
    	int t=66 + 1;
    	int l=154 + 1;
    	int cellw=154 - 3;
    	int sepw=3;
    	int cellh=67 - 3;
    	int seph=3;
    	int cols=7;
    	int rows=13;
    	
    	int x = l + getWeekDay(weekday) * (cellw + sepw);
    	double temp1 = getRow(start_time);
    	int y1 = (int) (t + getRow(start_time) * (cellh + seph));
    	int y2 = (int) (t + getRow(end_time) * (cellh + seph));
    	//int y2 = y1 + cellh;
    	
    	Rectangle result = new Rectangle(x, y1, cellw, y2 - y1);
    	return result;
    }
    
    private Rectangle convertToDisplay(Rectangle rc)
    {
    	int w=1235;
    	int h=1000;
    	
    	int x = (int) (rc.getX() / (double)h * image_h);
    	int y = (int) (rc.getY() / (double)h * image_h);
    	int h1 = (int) (rc.getSize().getWidth() / (double)h * image_h);
    	int w1 = (int) (rc.getSize().getHeight() / (double)h * image_h);
    	return new Rectangle(x, y, h1, w1);
    }
    
    private void updateContent(final Dialog progress) {
        try {
            ConnectionRequest req = new ConnectionRequest() {
                protected void readResponse(InputStream input) throws IOException {
                	try{
                		JSONParser p = new JSONParser();
                		Hashtable h = p.parse(new InputStreamReader(input));

                		final Vector v = (Vector) h.get("root");
                		for (int i = 0; i < v.size(); i++) {
                			Hashtable entry = (Hashtable) v.elementAt(i);
                			String weekday = (String) entry.get("weekday");
                			String weekday_chinese = (String) entry.get("weekday_chinese");
                			final String start_time = (String) entry.get("start_time");
                			final String end_time = (String) entry.get("end_time");
                			final String lesson = (String) entry.get("lesson");
                			final String lesson_chinese = (String) entry.get("lesson_chinese");
                			String site = (String) entry.get("site");
                			String site_address = (String) entry.get("site_address");
                			final String teacher = (String) entry.get("teacher");         
                			Button l = new Button(lesson);
                			
                			Rectangle rc = convertToDisplay(calculatePosition(weekday, start_time, end_time)); 
                			l.setX(rc.getX());
                			l.setY(rc.getY());
                			l.setPreferredSize(rc.getSize());
                			l.setUIID("Following");
                			l.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									String text = lesson_chinese + "\n" + 
											start_time + " ~ " + end_time + "\n" +
											teacher;
									Dialog.show(lesson, text, Dialog.TYPE_INFO, null, "OK", null);
								}
                			});
                			mainform.addComponent(l);

                		}
                		progress.dispose();
                	}
                	catch(IOException ex) {
                		ex.printStackTrace();
                	}
                }
            };
            req.setUrl("http://timetable.sinaapp.com");
            req.setPost(false);
            NetworkManager.getInstance().addToQueue(req);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
