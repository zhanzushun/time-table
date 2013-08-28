/**
 * This class contains generated code from the Codename One Designer, DO NOT MODIFY!
 * This class is designed for subclassing that way the code generator can overwrite it
 * anytime without erasing your changes which should exist in a subclass!
 * For details about this file and how it works please read this blog post:
 * http://codenameone.blogspot.com/2010/10/ui-builder-class-how-to-actually-use.html
*/
package generated;

import com.codename1.ui.*;
import com.codename1.ui.util.*;
import com.codename1.ui.plaf.*;
import com.codename1.ui.events.*;

public abstract class StateMachineBase extends UIBuilder {
    private Container aboutToShowThisContainer;
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    /**
    * @deprecated use the version that accepts a resource as an argument instead
    
**/
    protected void initVars() {}

    protected void initVars(Resources res) {}

    public StateMachineBase(Resources res, String resPath, boolean loadTheme) {
        startApp(res, resPath, loadTheme);
    }

    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("GenericSpinner", com.codename1.ui.spinner.GenericSpinner.class);
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("Ads", com.codename1.components.Ads.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("Dialog", com.codename1.ui.Dialog.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    if(resPath.endsWith(".res")) {
                        res = Resources.open(resPath);
                        System.out.println("Warning: you should construct the state machine without the .res extension to allow theme overlays");
                    } else {
                        res = Resources.openLayered(resPath);
                    }
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        if(res != null) {
            setResourceFilePath(resPath);
            setResourceFile(res);
            initVars(res);
            return showForm(getFirstFormName(), null);
        } else {
            Form f = (Form)createContainer(resPath, getFirstFormName());
            initVars(fetchResourceFile());
            beforeShow(f);
            f.show();
            postShow(f);
            return f;
        }
    }

    protected String getFirstFormName() {
        return "Main";
    }

    public Container createWidget(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("GenericSpinner", com.codename1.ui.spinner.GenericSpinner.class);
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("Ads", com.codename1.components.Ads.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("Dialog", com.codename1.ui.Dialog.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    res = Resources.openLayered(resPath);
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        return createContainer(resPath, "Main");
    }

    protected void initTheme(Resources res) {
            String[] themes = res.getThemeResourceNames();
            if(themes != null && themes.length > 0) {
                UIManager.getInstance().setThemeProps(res.getTheme(themes[0]));
            }
    }

    public StateMachineBase() {
    }

    public StateMachineBase(String resPath) {
        this(null, resPath, true);
    }

    public StateMachineBase(Resources res) {
        this(res, null, true);
    }

    public StateMachineBase(String resPath, boolean loadTheme) {
        this(null, resPath, loadTheme);
    }

    public StateMachineBase(Resources res, boolean loadTheme) {
        this(res, null, loadTheme);
    }

    public com.codename1.ui.Container findContainer4(Component root) {
        return (com.codename1.ui.Container)findByName("Container4", root);
    }

    public com.codename1.ui.Container findContainer4() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container4", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container4", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer3(Component root) {
        return (com.codename1.ui.Container)findByName("Container3", root);
    }

    public com.codename1.ui.Container findContainer3() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container3", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container3", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.spinner.GenericSpinner findRoomSpinner(Component root) {
        return (com.codename1.ui.spinner.GenericSpinner)findByName("RoomSpinner", root);
    }

    public com.codename1.ui.spinner.GenericSpinner findRoomSpinner() {
        com.codename1.ui.spinner.GenericSpinner cmp = (com.codename1.ui.spinner.GenericSpinner)findByName("RoomSpinner", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.spinner.GenericSpinner)findByName("RoomSpinner", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer2(Component root) {
        return (com.codename1.ui.Container)findByName("Container2", root);
    }

    public com.codename1.ui.Container findContainer2() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container2", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container2", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findSubAreaLabel(Component root) {
        return (com.codename1.ui.Label)findByName("SubAreaLabel", root);
    }

    public com.codename1.ui.Label findSubAreaLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("SubAreaLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("SubAreaLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer1(Component root) {
        return (com.codename1.ui.Container)findByName("Container1", root);
    }

    public com.codename1.ui.Container findContainer1() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findMainContainer(Component root) {
        return (com.codename1.ui.Container)findByName("MainContainer", root);
    }

    public com.codename1.ui.Container findMainContainer() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("MainContainer", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("MainContainer", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer5(Component root) {
        return (com.codename1.ui.Container)findByName("Container5", root);
    }

    public com.codename1.ui.Container findContainer5() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container5", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container5", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.Ads findAds(Component root) {
        return (com.codename1.components.Ads)findByName("Ads", root);
    }

    public com.codename1.components.Ads findAds() {
        com.codename1.components.Ads cmp = (com.codename1.components.Ads)findByName("Ads", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.Ads)findByName("Ads", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.spinner.GenericSpinner findClubSpinner(Component root) {
        return (com.codename1.ui.spinner.GenericSpinner)findByName("ClubSpinner", root);
    }

    public com.codename1.ui.spinner.GenericSpinner findClubSpinner() {
        com.codename1.ui.spinner.GenericSpinner cmp = (com.codename1.ui.spinner.GenericSpinner)findByName("ClubSpinner", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.spinner.GenericSpinner)findByName("ClubSpinner", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findDialogLabel(Component root) {
        return (com.codename1.ui.TextArea)findByName("DialogLabel", root);
    }

    public com.codename1.ui.TextArea findDialogLabel() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("DialogLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("DialogLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findButton(Component root) {
        return (com.codename1.ui.Button)findByName("Button", root);
    }

    public com.codename1.ui.Button findButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Button", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Button", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findAreaLabel(Component root) {
        return (com.codename1.ui.Label)findByName("AreaLabel", root);
    }

    public com.codename1.ui.Label findAreaLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("AreaLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("AreaLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel1(Component root) {
        return (com.codename1.ui.Label)findByName("Label1", root);
    }

    public com.codename1.ui.Label findLabel1() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel2(Component root) {
        return (com.codename1.ui.Label)findByName("Label2", root);
    }

    public com.codename1.ui.Label findLabel2() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label2", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label2", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findClubLabel(Component root) {
        return (com.codename1.ui.Label)findByName("ClubLabel", root);
    }

    public com.codename1.ui.Label findClubLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("ClubLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("ClubLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findRoomLabel(Component root) {
        return (com.codename1.ui.Label)findByName("RoomLabel", root);
    }

    public com.codename1.ui.Label findRoomLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("RoomLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("RoomLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel(Component root) {
        return (com.codename1.ui.Label)findByName("Label", root);
    }

    public com.codename1.ui.Label findLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.spinner.GenericSpinner findAreaSpinner(Component root) {
        return (com.codename1.ui.spinner.GenericSpinner)findByName("AreaSpinner", root);
    }

    public com.codename1.ui.spinner.GenericSpinner findAreaSpinner() {
        com.codename1.ui.spinner.GenericSpinner cmp = (com.codename1.ui.spinner.GenericSpinner)findByName("AreaSpinner", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.spinner.GenericSpinner)findByName("AreaSpinner", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer(Component root) {
        return (com.codename1.ui.Container)findByName("Container", root);
    }

    public com.codename1.ui.Container findContainer() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.spinner.GenericSpinner findSubAreaSpinner(Component root) {
        return (com.codename1.ui.spinner.GenericSpinner)findByName("SubAreaSpinner", root);
    }

    public com.codename1.ui.spinner.GenericSpinner findSubAreaSpinner() {
        com.codename1.ui.spinner.GenericSpinner cmp = (com.codename1.ui.spinner.GenericSpinner)findByName("SubAreaSpinner", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.spinner.GenericSpinner)findByName("SubAreaSpinner", aboutToShowThisContainer);
        }
        return cmp;
    }

    public static final int COMMAND_GUI1 = 5;
    public static final int COMMAND_Main = 1;

    protected boolean onGUI1() {
        return false;
    }

    protected boolean onMain() {
        return false;
    }

    protected void processCommand(ActionEvent ev, Command cmd) {
        switch(cmd.getId()) {
            case COMMAND_GUI1:
                if(onGUI1()) {
                    ev.consume();
                    return;
                }
                break;

            case COMMAND_Main:
                if(onMain()) {
                    ev.consume();
                    return;
                }
                break;

        }
        if(ev.getComponent() != null) {
            handleComponentAction(ev.getComponent(), ev);
        }
    }

    protected void exitForm(Form f) {
        if("GUI 2".equals(f.getName())) {
            exitGUI2(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            exitMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("GUI 1".equals(f.getName())) {
            exitGUI1(f);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void exitGUI2(Form f) {
    }


    protected void exitMain(Form f) {
    }


    protected void exitGUI1(Form f) {
    }

    protected void beforeShow(Form f) {
    aboutToShowThisContainer = f;
        if("GUI 2".equals(f.getName())) {
            beforeGUI2(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            beforeMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("GUI 1".equals(f.getName())) {
            beforeGUI1(f);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void beforeGUI2(Form f) {
    }


    protected void beforeMain(Form f) {
    }


    protected void beforeGUI1(Form f) {
    }

    protected void beforeShowContainer(Container c) {
    aboutToShowThisContainer = c;
        if("GUI 2".equals(c.getName())) {
            beforeContainerGUI2(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(c.getName())) {
            beforeContainerMain(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("GUI 1".equals(c.getName())) {
            beforeContainerGUI1(c);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void beforeContainerGUI2(Container c) {
    }


    protected void beforeContainerMain(Container c) {
    }


    protected void beforeContainerGUI1(Container c) {
    }

    protected void postShow(Form f) {
        if("GUI 2".equals(f.getName())) {
            postGUI2(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            postMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("GUI 1".equals(f.getName())) {
            postGUI1(f);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void postGUI2(Form f) {
    }


    protected void postMain(Form f) {
    }


    protected void postGUI1(Form f) {
    }

    protected void postShowContainer(Container c) {
        if("GUI 2".equals(c.getName())) {
            postContainerGUI2(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(c.getName())) {
            postContainerMain(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("GUI 1".equals(c.getName())) {
            postContainerGUI1(c);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void postContainerGUI2(Container c) {
    }


    protected void postContainerMain(Container c) {
    }


    protected void postContainerGUI1(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if("GUI 2".equals(rootName)) {
            onCreateGUI2();
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(rootName)) {
            onCreateMain();
            aboutToShowThisContainer = null;
            return;
        }

        if("GUI 1".equals(rootName)) {
            onCreateGUI1();
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void onCreateGUI2() {
    }


    protected void onCreateMain() {
    }


    protected void onCreateGUI1() {
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if(rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        if(c.getParent().getLeadParent() != null) {
            c = c.getParent().getLeadParent();
        }
        if(rootContainerName == null) return;
        if(rootContainerName.equals("GUI 2")) {
            if("DialogLabel".equals(c.getName())) {
                onGUI2_DialogLabelAction(c, event);
                return;
            }
            if("Button".equals(c.getName())) {
                onGUI2_ButtonAction(c, event);
                return;
            }
        }
    }

      protected void onGUI2_DialogLabelAction(Component c, ActionEvent event) {
      }

      protected void onGUI2_ButtonAction(Component c, ActionEvent event) {
      }

}
