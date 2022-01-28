package lhn.ftp.client.schedual;

/**
 * 
 *
 */
public class FtpDownloadSchedualApp 
{
    public static void main( String[] args )
    {
    	if(args.length>0) {
    		System.out.println(args[0]);
    		if(args.length>1) {
    			System.out.println(args[1]);
    		}
    	} 
    	SchedualManager.start();
    }
}
