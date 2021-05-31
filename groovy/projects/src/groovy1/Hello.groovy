import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

def baseDir = "D:/work/project/msp/git/GUT/msp/coding/doc/know-how/flutter/flutter-fcm"
def srcfile = "firebase-push-message.md"
def bakfile = srcfile + '.bak'
def imgList = []

// backup file
Path srcp = Paths.get(baseDir, srcfile);
Path bakp = Paths.get(baseDir, bakfile);
Files.copy(srcp, bakp, StandardCopyOption.REPLACE_EXISTING)


int cnt = srcp.getNameCount()

println srcp.subpath(0, cnt-3)

new File(baseDir, srcfile).eachLine { line, nb ->
    if(line.indexOf("../../img")>=0 ) {
        String fname = getFileName(line)
		Path srcimg = Paths.get(srcp.subpath(0, cnt-3), "img");
		Files.move(source, target, options)
//        println line
    } else if(line.indexOf("../img")>=0 ) {
         println line
    }
       
}

def getFileName(String path) {
    int slashPos = path.lastIndexOf('img/');
    if(slashPos>=0) {
        path = path.substring(slashPos+4, path.length()-1)
    } else {
        slashPos = path.lastIndexOf('img\\');
        path =  path.substring(slashPos+4, path.length()-1)
    }
   return path.replace(")","")
}