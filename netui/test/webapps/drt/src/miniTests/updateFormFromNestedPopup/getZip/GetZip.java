package miniTests.updateFormFromNestedPopup.getZip;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;


@Jpf.Controller(
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class GetZip extends PageFlowController
{
    /**
     * This is the zip code and state that will be returned in {@link #done}.
     */
    private ZipForm _zip;
    
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="done",
                returnAction="zipSuccess",
                outputFormBean="_zip"
            )
        }
    )
    protected Forward done()
    {
    	return new Forward( "done" );
    }


    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="confirm", path="confirm.jsp"),
            @Jpf.Forward(name="invalidZip", path="invalidZip.jsp")
        },
        useFormBean="_zip",
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    protected Forward submitZip( ZipForm form )
    {
        int prefix = Integer.parseInt( form.getZip().substring( 0, 3 ) );
        String state = _zips[prefix];

        if ( state == null )
        {
            return new Forward( "invalidZip", "zip", form.getZip() );
        }

        _zip.setState( state );

        Forward fwd = new Forward( "confirm" );
        fwd.addActionOutput( "zip", form.getZip() );
        fwd.addActionOutput( "state", state );
        return fwd;
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="done", returnAction="zipCancel")
        },
        doValidation=false
    )
    protected Forward cancel( ZipForm form )
    {
        return new Forward( "done" );
    }

    @Jpf.FormBean()
    public static class ZipForm implements Serializable
    {
        private String _state;
        private String _zip;

        public ZipForm()
        {
        }

        @Jpf.ValidatableProperty(
            displayName="The zip code",
            validateRequired=@Jpf.ValidateRequired(),
            validateMinLength=@Jpf.ValidateMinLength(chars=5),
            validateMaxLength=@Jpf.ValidateMaxLength(chars=5),
            validateMask=@Jpf.ValidateMask(regex="[0-9][0-9]")
        )
        public String getZip()
        {
            return _zip;
        }

        public void setZip( String zip )
        {
            _zip = zip;
        }

        public String getState()
        {
            return _state;
        }

        public void setState( String state )
        {
            _state = state;
        }
    }

    private static String[] _zips = new String[1000];

    static
    {
        setZips( 5, 5, "New York" );
        setZips( 6, 7, "Puerto Rico" );
        setZips( 8, 8, "Virgin Islands" );
        setZips( 9, 9, "Puerto Rico" );
        setZips( 10, 27, "Massachusetts" );
        setZips( 28, 29, "Rhode Island" );
        setZips( 30, 38, "New Hampshire" );
        setZips( 39, 49, "Maine" );
        setZips( 50, 54, "Vermont" );
        setZips( 55, 55, "Massachusetts" );
        setZips( 56, 59, "Vermont" );
        setZips( 60, 69, "Connecticut" );
        setZips( 70, 89, "New Jersey" );
        setZips( 90, 99, "Armed Forces Europe*" );
        setZips( 100, 149, "New York" );
        setZips( 150, 196, "Pennsylvania" );
        setZips( 197, 199, "Delaware" );
        setZips( 200, 200, "District of Columbia" );
        setZips( 201, 201, "Virginia" );
        setZips( 202, 205, "District of Columbia" );
        setZips( 206, 212, "Maryland" );
        setZips( 214, 219, "Maryland" );
        setZips( 220, 246, "Virginia" );
        setZips( 247, 268, "West Virginia" );
        setZips( 270, 289, "North Carolina" );
        setZips( 290, 299, "South Carolina" );
        setZips( 300, 319, "Georgia" );
        setZips( 320, 339, "Florida" );
        setZips( 340, 340, "Armed Forces Americas" );
        setZips( 341, 342, "Florida" );
        setZips( 344, 344, "Florida" );
        setZips( 346, 347, "Florida" );
        setZips( 349, 349, "Florida" );
        setZips( 350, 352, "Alabama" );
        setZips( 354, 369, "Alabama" );
        setZips( 370, 385, "Tennessee" );
        setZips( 386, 397, "Mississippi" );
        setZips( 398, 399, "Georgia" );
        setZips( 399, 399, "Georgia" );
        setZips( 400, 427, "Kentucky" );
        setZips( 430, 459, "Ohio" );
        setZips( 460, 470, "Indiana" );
        setZips( 471, 471, "Kentucky" );
        setZips( 472, 479, "Indiana" );
        setZips( 480, 499, "Michigan" );
        setZips( 500, 528, "Iowa" );
        setZips( 530, 532, "Wisconsin" );
        setZips( 534, 535, "Wisconsin" );
        setZips( 537, 549, "Wisconsin" );
        setZips( 550, 551, "Minnesota" );
        setZips( 553, 566, "Minnesota" );
        setZips( 567, 567, "North Dakota" );
        setZips( 570, 577, "South Dakota" );
        setZips( 580, 588, "North Dakota" );
        setZips( 590, 599, "Montana" );
        setZips( 600, 620, "Illinois" );
        setZips( 622, 629, "Illinois" );
        setZips( 630, 631, "Missouri" );
        setZips( 633, 641, "Missouri" );
        setZips( 644, 658, "Missouri" );
        setZips( 660, 662, "Kansas" );
        setZips( 664, 679, "Kansas" );
        setZips( 680, 681, "Nebraska" );
        setZips( 683, 693, "Nebraska" );
        setZips( 700, 701, "Louisiana" );
        setZips( 703, 708, "Louisiana" );
        setZips( 710, 714, "Louisiana" );
        setZips( 716, 729, "Arkansas" );
        setZips( 730, 731, "Oklahoma" );
        setZips( 733, 733, "Texas" );
        setZips( 734, 741, "Oklahoma" );
        setZips( 743, 749, "Oklahoma" );
        setZips( 750, 799, "Texas" );
        setZips( 800, 816, "Colorado" );
        setZips( 820, 831, "Wyoming" );
        setZips( 832, 838, "Idaho" );
        setZips( 840, 847, "Utah" );
        setZips( 850, 850, "Arizona" );
        setZips( 852, 853, "Arizona" );
        setZips( 855, 857, "Arizona" );
        setZips( 859, 860, "Arizona" );
        setZips( 863, 865, "Arizona" );
        setZips( 870, 875, "New Mexico" );
        setZips( 877, 884, "New Mexico" );
        setZips( 885, 885, "Texas" );
        setZips( 889, 891, "Nevada" );
        setZips( 893, 895, "Nevada" );
        setZips( 897, 898, "Nevada" );
        setZips( 900, 908, "California" );
        setZips( 910, 928, "California" );
        setZips( 930, 961, "California" );
        setZips( 962, 966, "Armed Forces Pacific" );
        setZips( 967, 968, "Hawaii" );
        setZips( 969, 969, "Guam" );
        setZips( 970, 979, "Oregon" );
        setZips( 980, 986, "Washington" );
        setZips( 988, 994, "Washington" );
        setZips( 995, 999, "Alaska" );
    }

    private static void setZips( int start, int end, String state )
    {
        for ( int i = start; i <= end; ++i )
        {
            _zips[i] = state;
        }
    }
}
