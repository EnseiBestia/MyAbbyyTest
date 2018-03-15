package demo.abbyy;
import com.abbyy.FCEngine.*;

import java.io.File;
import java.net.URL;


public class FlexiCaptureTest {
    public static String serialNumber = "SWTT-1102-0006-4136-5461-8187";
    public  static String dllPath="X:\\Program Files (x86)\\ABBYY SDK\\11\\FlexiCapture Engine\\Bin";
    public static void main( String[] args )
    {

        try {
            URL url = FlexiCaptureTest.class.getClassLoader().getResource("");
            File f = new File(url.getFile());
            String samplesFolder = f.getAbsolutePath();
            trace("config file path:"+samplesFolder);
            trace( "Load the engine..." );
            IEngine engine = Engine.Load( dllPath, serialNumber, "" );

            try{

                trace( "Create and configure FlexiCaptureProcessor..." );
                IFlexiCaptureProcessor processor = engine.CreateFlexiCaptureProcessor();
                IDocumentDefinition idd = engine.CreateDocumentDefinitionFromAFL(samplesFolder+ "\\ttfl.afl","ChinesePRC");
                processor.AddDocumentDefinition(idd);

                trace( "Add image files to recognize..." );
                processor.AddImageFile( samplesFolder + "\\安全生产许可证.jpg" );

                trace( "Recognize document..." );
                IDocument document = processor.RecognizeNextDocument();
                IDocumentDefinition definition = document.getDocumentDefinition();
                assert( definition != null );
                assert( document.getPages().getCount() == 1 );

                trace( "====================================" );
                trace( "DocumentType: " + document.getDocumentDefinition().getName() );
                trace( "====================================" );
                IFields fields = document.getSections().Item( 0 ).getChildren();
                for( int i = 0; i < fields.getCount(); i++ ) {
                    IField field = fields.getElement( i );
                    trace( field.getName() + ": " +
                            ( field.getValue() != null ? field.getValue().getAsString() : "." ) );
                }
                trace( "====================================" );

            } finally {
                Engine.Unload();
            }

            trace( "Done." );
        } catch( Exception ex ) {
            trace( ex.getMessage() );
            ex.printStackTrace();
        }
    }

    private static void trace( String txt )
    {
        System.out.println( txt );
    }

}
