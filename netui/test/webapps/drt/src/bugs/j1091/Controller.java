package bugs.j1091;

import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller()
public class Controller
    extends PageFlowController {
    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "index",
                     path = "index.jsp")
    })
    protected Forward begin()
    {
        return new Forward("index");
    }
    
    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward submit(ColorForm form)
    {
        return new Forward("success");
    }

    public static class ColorForm extends FormData {
        private String _color = null;
        private String _shade = null;
        private String[] _colors = null;
        
        public String getShade() {
			return _shade;
		}

		public void setShade(String shade) {
			_shade = shade;
		}

		public String getColor() {
            return _color;
        }
        
        public void setColor(String color) {
            _color = color;
        }

        public String[] getColors() {
            return _colors;
        }

        public void setColors(String[] colors) {
            _colors = colors;
        }
    }
}
