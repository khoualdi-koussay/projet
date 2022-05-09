/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import models.message;
import services.message_service;

/**
 *
 * @author khoualdi koussay
 */
public class message_form extends Form{

    Resources theme = UIManager.initFirstTheme("/theme");
        int id_res=0;
;
        int id_sender=0;
                public message_form(Form previous,int id_r , int id_s) {
                  
            super("Message", BoxLayout.y());
              id_res=id_r;
                    id_sender=id_s;
                this.add(new InfiniteProgress());
        Display.getInstance().scheduleBackgroundTask(() -> {
            // this will take a while...

            Display.getInstance().callSerially(() -> {
                this.removeAll();
             
              //  this.add(logi);

                for (message c : new message_service().getmessage(id_r)) {

                    this.add(addIteam_message(c));
                }
             

                this.revalidate();
            });
        });

        this.getToolbar().addSearchCommand(e -> {
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                // clear search
                for (Component cmp : this.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
                this.getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                for (Component cmp : this.getContentPane()) {
                    MultiButton mb = (MultiButton) cmp;
                    String line1 = mb.getTextLine1();
                    String line2 = mb.getTextLine2();
                  
                    boolean show = line1 != null && line1.toLowerCase().indexOf(text) > -1
                            || line2 != null && line2.toLowerCase().indexOf(text) > -1;
                    mb.setHidden(!show);
                    mb.setVisible(show);
                }
                this.getContentPane().animateLayout(150);
            }
        }, 4);
        
                this.getToolbar().addCommandToOverflowMenu("Add", null, ev -> {

            Form ajout = new Form("Add message",BoxLayout.y());
            
            TextField message = new TextField("", "message", 20, TextArea.ANY);
          ComboBox cmb = new ComboBox();
        if (id_res == 1)
        {
           cmb.addItem("koussay.khoualdi@esprit.tn");  
        }
        else {
            cmb.addItem("oussama.khamasi@esprit.tn");  
        }
     

            ajout.add("message : ").add(message).add("email : ").add(cmb);
          Button submit = new Button("Submit");
            ajout.add(submit);
          
            ajout.getToolbar().addCommandToLeftBar("back",null, evx -> {
                this.showBack();
            });
             Validator val_message = new Validator();

            val_message.addConstraint(message, new LengthConstraint(8));

            String text_saisir_des_caracteres = "^[0-9]+$";

            val_message.addConstraint(message, new RegexConstraint(text_saisir_des_caracteres, ""));

            submit.addActionListener(aj
                    -> {
                  if (message.getText().equals("")) {
                                    Dialog.show("Erreur", "Champ vide de message ", "OK", null);

                                } else if (val_message.isValid()) {
                                    Dialog.show("Erreur message !", "il faut saisir des caracteres  !", "OK", null);
                                }
                                else {
                                    new message_service().add_message(id_r, id_s, message.getText());
                                                               
                            // api
                            ToastBar.Status status = ToastBar.getInstance().createStatus();
  status.setMessage("Add Success");

  status.show();
  new message_form(ajout, id_res, id_res).showBack();
                                }

            }
            );
       

            ajout.show();

        });
        
    }
    
    public MultiButton addIteam_message(message c) {
        MultiButton m = new MultiButton();
     
        String url = "http://localhost/icon.png";
       m.setTextLine1(c.getMessage());
        m.setTextLine2(c.getCreated_at());
  
   
          
        m.setEmblem(theme.getImage("round.png"));
        Image imge;
        EncodedImage enc;
        enc = EncodedImage.createFromImage(theme.getImage("round.png"), false);
        imge = URLImage.createToStorage(enc, url, url);
        m.setIcon(imge);
     m.addActionListener(l
                -> {

                   Form f2 = new Form("Message details",BoxLayout.y());
           

            f2.getToolbar().addCommandToLeftBar("back",null, (evt) -> {
                this.showBack();
            });
        
           Button Supprimer = new Button("supprimer");
             
               Supprimer.addActionListener(ev
                                -> {
                            new message_service().delete_message(id_res,c.getId());
                            // metier
                            Dialog.show("Delete", "Delete ", "OK", null);
                            // api
                            ToastBar.Status status = ToastBar.getInstance().createStatus();
  status.setMessage("Delete Success");

  status.show();
  new message_form(f2, id_res, id_res).showBack();
                        }
                        );
       
              f2.add(imge).add("Message : ").add(c.getMessage()).add("Created at : ").add(c.getCreated_at()).add(Supprimer);

       
            f2.show();

        }
        );

        return m;
    }
    
}
