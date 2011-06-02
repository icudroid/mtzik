package fr.k2i.adbeback.webapp.taglib;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * <p>This class is designed to render a <label> tag for labeling your forms and
 * adds an asterik (*) for required fields.  It was originally written by Erik
 * Hatcher (http://www.ehatchersolutions.com/JavaDevWithAnt/).
 *
 * <p>It is designed to be used as follows:
 * <pre>&lt;tag:label key="userForm.username"/&gt;</pre>
 *
 * @jsp.tag name="formatTime" bodycontent="empty"
 */
public class FormatLongToTime extends TagSupport{
	private static final long serialVersionUID = -4304660247944860432L;
	protected Long duration;
	
    @Override
    public int doStartTag() throws JspException {
        DecimalFormat df = new DecimalFormat("00");
            StringBuffer sb = new StringBuffer();
            long hour = duration/3600;
            long rest = duration%3600;
            if(hour>0){
            	sb.append(df.format(hour));
            	sb.append(":");
            }
            long minutes = rest /60;
            rest = duration%60;
            sb.append(df.format(minutes));
            sb.append(":");
            sb.append(df.format(rest));	
            try {
                pageContext.getOut().write(sb.toString());
            } catch (IOException io) {
                throw new JspException(io);
            }

        return super.doStartTag();
    }
    
    /**
     * @jsp.attribute required="true" rtexprvalue="true"
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }
    
    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        duration = null;
    }
}
