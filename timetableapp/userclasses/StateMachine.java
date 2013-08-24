/**
 * Your application code goes here
 */

package userclasses;

import generated.StateMachineBase;
import com.codename1.ui.*; 
import com.codename1.ui.events.*;
import com.codename1.ui.layouts.CoordinateLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.spinner.GenericSpinner;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.UITimer;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;

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
	
	private String m_area;
	private String m_subArea;
	private String m_club;
	private String m_room;
	private boolean m_optionsDirty;
	
	private String areaListVersion;
	private String lessonListVersion;
	
	private Vector areaList;
	private Vector lessonList;

	
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
		try{
			m_area = (String) Storage.getInstance().readObject("area");
			m_subArea = (String) Storage.getInstance().readObject("subarea");
			m_club = (String) Storage.getInstance().readObject("club");
			m_room = (String) Storage.getInstance().readObject("room");
			m_optionsDirty = false;

			areaListVersion = (String) Storage.getInstance().readObject("areas_version");
			lessonListVersion = (String) Storage.getInstance().readObject("lessons_version");

			areaList = (Vector) Storage.getInstance().readObject("areas");
			lessonList = (Vector) Storage.getInstance().readObject("lessons");
		}
        catch (Exception ex) {
            ex.printStackTrace();
        }
	}

    @Override
    protected void postMain(Form f) {
    	mainform = f;
        UITimer ut = new UITimer(new Runnable() {
            public void run() {
            	try {
            		setCoordinateLayout();
            		if (m_optionsDirty) {
            			getLessonList();
            			m_optionsDirty = false;
            			return;
            		}
            		try {
            			getAreaListVersion();
            		}
            		catch (Exception ex) {
            			if (lessonList != null && lessonList.size() > 0)
            				refreshUI();
            		}
        		}
                catch (Exception ex) {
                    ex.printStackTrace();
                }            	
            }
        });
        ut.schedule(10, false, f);
    }
    
    private void setCoordinateLayout(){
    	Label l = findLabel();
    	image_h = l.getIcon().getHeight();
    	int display_w = Display.getInstance().getDisplayWidth();
    	int display_h = Display.getInstance().getDisplayHeight();
    	int title_h = mainform.getTitleArea().getPreferredH();
    	mainform.setLayout(new CoordinateLayout(display_w, display_h - title_h));
    	mainform.setX(0);
    	mainform.setY(0);
    }    
    
    private void getAreaListVersion() {
        try {
            ConnectionRequest req = new ConnectionRequest() {
                protected void readResponse(InputStream input) throws IOException {
                	try{
                		String newAreaListVersion = null;
                		
                		JSONParser p = new JSONParser();
                		Hashtable h = p.parse(new InputStreamReader(input));
                		Vector versionList = (Vector) h.get("root");
                		for (int i = 0; i < versionList.size(); i++) {
                			Hashtable entry = (Hashtable) versionList.elementAt(i);
                			newAreaListVersion = (String)(entry.get("version"));
                		}
                		
                		if (!newAreaListVersion.equals(areaListVersion)){
                			areaListVersion = newAreaListVersion;
                			onAreaListVersionUpdated();
                		}
                		else {
                			checkAreaListAndOptions();
                		}
                	}
                	catch(IOException ex) {
                		ex.printStackTrace();
                	}
                }
            };
            req.setUrl("http://timetable.sinaapp.com/areas/version");
            req.setPost(false);
            NetworkManager.getInstance().addToQueue(req);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void checkAreaListAndOptions() {
    	if (areaList == null)
    		getAreaList();
    	else
    		checkOptionsAndLessonsVersion();
    }
    private void onAreaListVersionUpdated() {
		getAreaList();
    }
    
    private void getAreaList() {
        try {
            ConnectionRequest req = new ConnectionRequest() {
                protected void readResponse(InputStream input) throws IOException {
                	try{
                		JSONParser p = new JSONParser();
                		Hashtable h = p.parse(new InputStreamReader(input));
                		areaList = (Vector) h.get("root");
                		onAreaListUpdated();
                	}
                	catch(IOException ex) {
                		ex.printStackTrace();
                	}
                }
            };
            req.setUrl("http://timetable.sinaapp.com/areas");
            req.setPost(false);
            NetworkManager.getInstance().addToQueue(req);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void onAreaListUpdated() {
    	Storage.getInstance().writeObject("areas", areaList);
    	Storage.getInstance().writeObject("areas_version", areaListVersion);
    	checkOptionsAndLessonsVersion();
    }
    
    private void checkOptionsAndLessonsVersion() {
		if (m_area == null || m_subArea == null || m_club == null || m_room == null)
			gotoOptionsForm();
		else
			getLessonListVersion();
    }
    
    private void gotoOptionsForm() {
    	Form optionsForm = (Form)createContainer("/theme.res", "GUI 1");
    	postGUI1(optionsForm);
    	optionsForm.show();
    }
    
    @Override
    protected void postGUI1(Form f) {
    	mainform = f;
        UITimer ut = new UITimer(new Runnable() {
            public void run() {
            	try {
            		initListModelAreaSpinner(findAreaSpinner());
            		initListModelSubAreaSpinner(findSubAreaSpinner());
            		initListModelClubSpinner(findClubSpinner());
            		initListModelRoomSpinner(findRoomSpinner());
            	}
            	catch (Exception ex) {
            		ex.printStackTrace();
            	}
            }
        });
        ut.schedule(10, false, f);
    }
    
    @Override    
    protected void exitGUI1(Form f) {
    	if (m_optionsDirty) {
    		try {
    			Storage.getInstance().writeObject("area", m_area);
    			Storage.getInstance().writeObject("subarea", m_subArea);
    			Storage.getInstance().writeObject("club", m_club);
    			Storage.getInstance().writeObject("room", m_room);
//    			if (m_area != null && m_subArea != null && m_club != null && m_room != null)
//    				getLessonListVersion();
    		}
    		catch (Exception ex) {
    			ex.printStackTrace();
    		}    		
    	}	
    }
    
    private void getLessonListVersion() {
        try {
            ConnectionRequest req = new ConnectionRequest() {
                protected void readResponse(InputStream input) throws IOException {
                	try{
                		String newVersion = null;
                		
                		JSONParser p = new JSONParser();
                		Hashtable h = p.parse(new InputStreamReader(input));
                		Vector versionList = (Vector) h.get("root");
                		for (int i = 0; i < versionList.size(); i++) {
                			Hashtable entry = (Hashtable) versionList.elementAt(i);
                			newVersion = (String)(entry.get("version"));
                		}
                		
                		if (!newVersion.equals(lessonListVersion)){
                			lessonListVersion = newVersion;
                			onLessonListVersionUpdated();
                		}
                		else
                			checkLessonList();
                	}
                	catch(IOException ex) {
                		ex.printStackTrace();
                	}
                }
            };
            req.setUrl("http://timetable.sinaapp.com/lessons/version/" + areaId() + "_"
            		+ subAreaId() + "_" + clubId() + "_" + roomId());
            req.setPost(false);
            NetworkManager.getInstance().addToQueue(req);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void checkLessonList() {
    	if (lessonList == null || m_optionsDirty) {
    		getLessonList();
    		m_optionsDirty = false;
    	}
    }
    
    private void onLessonListVersionUpdated() {
    	Storage.getInstance().writeObject("lessons_version", lessonListVersion);
    	getLessonList();
    }
    
    private void getLessonList() {
        InfiniteProgress inf = new InfiniteProgress();
        Dialog progress = inf.showInifiniteBlocking();
        getLessonList(progress);
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
    
    private Rectangle calculatePosition(String weekday, String start_time, String duration)
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
    	
    	int x = (int) (l + Integer.parseInt(weekday) * (cellw + sepw));
    	double temp1 = getRow(start_time);
    	int y1 = (int) (t + getRow(start_time) * (cellh + seph));
    	//int height = (int) ((Integer.parseInt(duration) / 60.0) * cellh);
    	int height = cellh;
    	
    	Rectangle result = new Rectangle(x, y1, cellw, height);
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
    
    private void getLessonList(final Dialog progress) {
        try {
            ConnectionRequest req = new ConnectionRequest() {
                protected void readResponse(InputStream input) throws IOException {
                	try{
                		JSONParser p = new JSONParser();
                		Hashtable h = p.parse(new InputStreamReader(input));
                		lessonList = (Vector) h.get("root");
                		progress.dispose();
                		onLessonListUpdated();
                	}
                	catch(IOException ex) {
                		ex.printStackTrace();
                	}
                }
            };
            req.setUrl("http://timetable.sinaapp.com/lessons/" + areaId() + "_" + subAreaId() + "_" + 
            		clubId() + "_" + roomId());
            req.setPost(false);
            NetworkManager.getInstance().addToQueue(req);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onLessonListUpdated() {
    	Storage.getInstance().writeObject("lessons", lessonList);
    	refreshUI();
    }
    
    private void refreshUI(){
		for (int i = 0; i < lessonList.size(); i++) {
			Hashtable entry = (Hashtable) lessonList.elementAt(i);
			
			String weekday = (String)(entry.get("weekday"));
			final String start_time = (String) entry.get("time");
			final String durationMin = (String) entry.get("duration");
			String duration = durationMin.substring(0, durationMin.length() - "MIN".length());
			final String lesson = (String) entry.get("lesson");
			final String lesson_chinese = (String) entry.get("lesson_chinese");
			final String teacher = (String) entry.get("teacher");
			
			Rectangle rc = convertToDisplay(calculatePosition(weekday, start_time, duration)); 

			Button l = new Button(lesson_chinese);
			l.setX(rc.getX());
			l.setY(rc.getY());
			l.setPreferredSize(rc.getSize());
			l.setUIID("Following");
			l.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String text = lesson_chinese + "\n" + 
							start_time + ", " + durationMin + "\n" +
							teacher;
					Dialog.show(lesson, text, Dialog.TYPE_INFO, null, "OK", null);
				}
			});
			l.setCellRenderer(true);
			mainform.addComponent(l);
		}
		mainform.getContentPane().repaint();
		if (m_club == null)
			mainform.setTitle("一兆韦德课程表");
		else
			mainform.setTitle(m_club);
    }

    private boolean initListModelAreaSpinner(GenericSpinner cmp) {
    	int selected = -1;
    	Vector strings = new Vector();
    	for (int i = 0; i< areaList.size(); i++){
    		Hashtable dict = (Hashtable)(areaList.get(i));
    		String str = (String) dict.get("area");
    		strings.add(str);
    		if (str.equals(m_area))
    			selected = i;
    	}
    	ListModel model = new DefaultListModel(strings);
    	if (selected == -1) {
    		selected = 0;
    	}
    	model.addSelectionListener(new SelectionListener() {
    		public void selectionChanged(int oldSelected, int newSelected) {
    			try {
    				if (findAreaSpinner() == null || findAreaSpinner().getModel() == null)
    					return;
    				String newArea = (String) findAreaSpinner().getModel().
    						getItemAt(findAreaSpinner().getModel().getSelectedIndex());
    				if (newArea == null || !newArea.equals(m_area)) {
    					m_area = newArea;
    					m_optionsDirty = true;
    					initListModelSubAreaSpinner(findSubAreaSpinner());
    				}
    			}
    			catch (Exception ex) {
    				ex.printStackTrace();
    			}
    		}
    	});
    	cmp.setModel(model);
    	model.setSelectedIndex(selected);
        return true;
    }
    
    private boolean initListModelSubAreaSpinner(GenericSpinner cmp) {
    	int selected = -1;
    	Vector strings = new Vector();
    	if (getArea() == null)
    		return true;
    	Vector subAreaList = (Vector) getArea().get("subareas");
    	
    	for (int i = 0; i<subAreaList.size(); i++){
    		Hashtable dict = (Hashtable)(subAreaList.get(i));
    		String str = (String) dict.get("subarea");
    		strings.add(str);
    		if (str.equals(m_subArea))
    			selected = i;
    	}
    	ListModel model = new DefaultListModel(strings);
    	if (selected == -1) {
    		selected = 0;
    	}
    	model.addSelectionListener(new SelectionListener() {
			public void selectionChanged(int oldSelected, int newSelected) {
				try {
    				if (findSubAreaSpinner() == null || findSubAreaSpinner().getModel() == null)
    					return;

					String newSubArea = (String) findSubAreaSpinner().getModel().
							getItemAt(findSubAreaSpinner().getModel().getSelectedIndex());
					if (newSubArea == null || !newSubArea.equals(m_subArea)) {
						m_subArea = newSubArea;
						m_optionsDirty = true;
						initListModelClubSpinner(findClubSpinner());
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}		
			}
    	});
    	cmp.setModel(model);
    	model.setSelectedIndex(selected);
        return true;
    }
    
    private boolean initListModelClubSpinner(GenericSpinner cmp) {
    	int selected = -1;
    	Vector strings = new Vector();
    	if (getSubArea() == null)
    		return true;
    	Vector clubList = (Vector) getSubArea().get("clubs");
    	
    	for (int i = 0; i< clubList.size(); i++){
    		Hashtable dict = (Hashtable)(clubList.get(i));
    		String str = (String) dict.get("club");
    		strings.add(str);
    		if (str.equals(m_club))
    			selected = i;
    	}
    	ListModel model = new DefaultListModel(strings);
    	if (selected == -1) {
    		selected = 0;
    	}
    	model.addSelectionListener(new SelectionListener() {
			public void selectionChanged(int oldSelected, int newSelected) {
				try {
    				if (findClubSpinner() == null || findClubSpinner().getModel() == null)
    					return;
					String newClub = (String) findClubSpinner().getModel().
							getItemAt(findClubSpinner().getModel().getSelectedIndex());
					if (newClub == null || !newClub.equals(m_club)) {
						m_club = newClub;
						m_optionsDirty = true;
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
    	});
    	cmp.setModel(model);
    	model.setSelectedIndex(selected);
        return true;
    } 
    
    private boolean initListModelRoomSpinner(GenericSpinner cmp) {
    	int selected = -1;
    	Vector strings = new Vector();
    	if (getSubArea() == null)
    		return true;
    	Vector clubList = (Vector) getSubArea().get("clubs");
    	
    	for (int i = 0; i< 3; i++){
    		String str = null;
    		if (i == 0)
    			str = "瑜伽教室";
    		else if (i == 1)
    			str = "有氧教室";
    		else if (i == 2)
    			str = "单车教室";
    		strings.add(str);
    		if (str.equals(m_room))
    			selected = i;
    	}
    	ListModel model = new DefaultListModel(strings);
    	if (selected == -1) {
    		selected = 0;
    	}
    	model.addSelectionListener(new SelectionListener() {
			public void selectionChanged(int oldSelected, int newSelected) {
				try {
    				if (findRoomSpinner() == null || findRoomSpinner().getModel() == null)
    					return;
					String newRoom = (String) findRoomSpinner().getModel().
							getItemAt(findRoomSpinner().getModel().getSelectedIndex());
					if (!newRoom.equals(m_room)) {
						m_room = newRoom;
						m_optionsDirty = true;
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
    	});
    	cmp.setModel(model);
    	model.setSelectedIndex(selected);
        return true;
    } 

    
    private Hashtable getArea() {
    	for (int i = 0; i< areaList.size(); i++){
    		Hashtable area = (Hashtable)(areaList.get(i));
    		if (area.get("area").equals(m_area))
    			return area;
    	}
    	return null;
    }
    
    private String areaId() {
    	if (getArea() == null)
    		return null;
    	return (String) getArea().get("area_id");
    }
    
    private Hashtable getSubArea() {
    	if (getArea() == null)
    		return null;
    	Vector subAreas = (Vector) getArea().get("subareas");
    	for (int i = 0; i< subAreas.size(); i++){
    		Hashtable subAreaDict = (Hashtable)(subAreas.get(i));
    		if (subAreaDict.get("subarea").equals(m_subArea)) {
    			return subAreaDict;
    		}
    	}
    	return null;
    }
    
    private String subAreaId() {
    	if (getSubArea() == null)
    		return null;
    	return (String) getSubArea().get("subarea_id");
    }
    
    private Hashtable getClub() {
    	if (getSubArea() == null)
    		return null;
    	Vector clubs = (Vector) getSubArea().get("clubs");
    	for (int i = 0; i< clubs.size(); i++){
    		Hashtable clubDict = (Hashtable)(clubs.get(i));
    		if (clubDict.get("club").equals(m_club)) {
    			return clubDict;
    		}
    	}
    	return null;
    }
    
    private String clubId() {
    	if (getClub() == null)
    		return null;
    	return (String) getClub().get("club_id");
    }
    
    private String roomId() {
    	if (m_room.equals("瑜伽教室"))
    		return "1";
    	if (m_room.equals("有氧教室"))
    		return "2";
    	if (m_room.equals("单车教室"))
    		return "3";
    	return "0";
    }     
}
