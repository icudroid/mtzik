package fr.k2i.adbeback.webapp.webenum;

import java.util.Locale;

import fr.k2i.adbeback.core.business.player.Sex;
import fr.k2i.adbeback.webapp.controller.web.BaseFormController;

public class WebEnum {
	
    public static String getText(Sex webenum,Locale locale) {
        return BaseFormController.messages.getMessage(webenum.getLabel(), locale);
    }
}
