/**
 * 
 */
package com.infostroy.paamns.install.classloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.infostroy.paamns.install.facade.InstallerUtil;



/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public final class UnpackJarClassLoader extends URLClassLoader
{
    /**
     * 
     */
    static private String       _toDir   = System.getProperty("java.io.tmpdir");
    
    /**
     * 
     */
    static private final String _baseDir = "paamns.tmp";
    
    /**import com.infostroy.paams.install.facade.InstallerUtil;

     * 
     */
    static
    {
        if (_toDir.charAt(_toDir.length() - 1) != '\\'
                && _toDir.charAt(_toDir.length() - 1) != '/')
        {
            _toDir += "/";
        }
        _toDir += _baseDir;

        InstallerUtil.deleteDirectory(new File(_toDir));
    }
    
    /**
     * 
     *
     * @author Sergey Manoylo
     * InfoStroy Co., 2010.
     *
     */
    private class ClassLodingContext
    {
        private Set<String> _files         = new HashSet<String>();
        
        private Set<String> _classPath     = new HashSet<String>();
        
        private Set<String> _postClassPath = new HashSet<String>();
        
        private IProcessJar _currProcessor = null;
        
        private IProcessJar _processor     = new ProcessJar(this);
        
        private IProcessJar _postProcessor = new PostProcessJar(this);
        
        /**
         * 
         * @return
         */
        public Set<String> getFiles()
        {
            return _files;
        }
        
        /**
         * 
         * @return
         */
        public Set<String> getClassPath()
        {
            return _classPath;
        }
        
        /**
         * 
         * @return
         */
        public Set<String> getPostClassPath()
        {
            return _postClassPath;
        }
        
        /**
         * 
         * @return
         */
        public IProcessJar getCurrProcessor()
        {
            return _currProcessor;
        }
        
        /**
         * 
         */
        public void setCurrProcessor()
        {
            _currProcessor = _processor;
        }
        
        /**
         * 
         */
        public void setCurrPostProcessor()
        {
            _currProcessor = _postProcessor;
        }
        
    }
    
    /**
     * 
     *currPath
     * @author Sergey Manoylo
     * InfoStroy Co., 2010.
     *
     */
    private interface IProcessJar
    {
        int processJar(String filePath) throws IOException;
    };
    
    /**
     * 
     *
     * @author Sergey Manoylo
     * InfoStroy Co., 2010.
     *
     */
    private class ProcessJar implements IProcessJar
    {
        private ClassLodingContext _clLdrCtx = null;
        
        /**
         * 
         * @param clLdrCtx
         */
        public ProcessJar(ClassLodingContext clLdrCtx)
        {
            _clLdrCtx = clLdrCtx;
        }
        
        /**
         * 
         */
        public int processJar(String filePath) throws IOException
        {
            int count = 0;
            
            JarFile jf = new JarFile(filePath);
            
            if(jf.getManifest() == null)
            {
                return count; 
            }
            
            Attributes att = jf.getManifest().getMainAttributes();
            
            String cpStr = att.getValue("Class-Path");
            
            if (cpStr != null && !cpStr.isEmpty())
            {
                StringTokenizer strTokenizer = new StringTokenizer(att
                        .getValue("Class-Path"), ",; ");
                
                Set<String> cps = new HashSet<String>();
                
                while (strTokenizer.hasMoreElements())
                {
                    String cpToken = strTokenizer.nextToken();
                    cps.add(cpToken);
                }
                
                ZipEntry ze = null;
                for (String cp : cps)
                {
                    ze = jf.getEntry(cp);
                    _clLdrCtx.getClassPath().add(cp);
                    if (ze != null)
                    {
                        InputStream iis = jf.getInputStream(ze);
                        count += UnpackJarClassLoader.this.unpackJar(_clLdrCtx,
                                iis, cp);
                    }
                    else
                    {
                        _clLdrCtx.getPostClassPath().add(cp);
                    }
                }
            }
            
            return count;
        }
    }
    
    /**
     * 
     *
     * @author Sergey Manoylo
     * InfoStroy Co., 2010.
     *
     */
    private class PostProcessJar implements IProcessJar
    {
        private ClassLodingContext _clLdrCtx = null;
        
        /**
         * 
         * @param clLdrCtx
         */
        public PostProcessJar(ClassLodingContext clLdrCtx)
        {
            _clLdrCtx = clLdrCtx;
        }
        
        /**
         * 
         */
        public int processJar(String filePath) throws IOException
        {
            int count = 0;
            
            JarFile jf = new JarFile(filePath);
            
            ZipEntry ze = null;
            
            List<String> filePostPathes = new LinkedList<String>();  

            for (String fp: _clLdrCtx.getPostClassPath())
            {
                filePostPathes.add(fp);
            }
            
            for (String pcp : filePostPathes)
            {
                ze = jf.getEntry(pcp);
                _clLdrCtx.getClassPath().add(pcp);
                if (ze != null)
                {
                    InputStream iis = jf.getInputStream(ze);
                    count += UnpackJarClassLoader.this.unpackJar(_clLdrCtx,
                            iis, pcp);
                    _clLdrCtx.getPostClassPath().remove(pcp);
                }
            }
            
            return count;
        }
    }
    
    /**
     * 
     * @param parent
     */
    public UnpackJarClassLoader(ClassLoader parent)
    {
        super(new URL[] {}, parent);
        initClassLoader();
    }
    
    /**
     * 
     */
    public UnpackJarClassLoader()
    {
        super(new URL[] {}, ClassLoader.getSystemClassLoader());
        initClassLoader();
    }
    
    /**
     * 
     */
    private void initClassLoader()
    {
        ClassLodingContext clLdCntx = this.new ClassLodingContext();
        
        getClassPath(clLdCntx);

        for (String path : clLdCntx.getFiles())
        {
            try
            {
                super.addURL(new File(path).toURI().toURL());
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /*
     * 
     */
    private int getClassPath(ClassLodingContext clLdCntx)
    {
        int count = 0;
        
        String currPath = UnpackJarClassLoader.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        
        if (!(new File(currPath).isDirectory()))
        {
            try
            {
                clLdCntx.setCurrProcessor();
                count += clLdCntx.getCurrProcessor().processJar(currPath);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            
            clLdCntx.setCurrPostProcessor();
            
            List<String> filePostPathes = new LinkedList<String>();
            
            for(String pcp : clLdCntx.getPostClassPath())
            {
                filePostPathes.add(pcp);
            }
            
            for (String classPath : filePostPathes)
            {
                if (clLdCntx.getFiles().contains(classPath))
                {
                    clLdCntx.getFiles().remove(classPath);
                }
                else
                {
                    List<String> filePathes = new LinkedList<String>();  

                    for (String filePath : clLdCntx.getFiles())
                    {
                        filePathes.add(filePath);
                    }
                    
                    for (String filePath : filePathes)
                    {
                        try
                        {
                            count += clLdCntx.getCurrProcessor().processJar(
                                    filePath);
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        
        return count;
    }

    /**
     * 
     * @param jarName
     * @throws IOException 
     */
    private int unpackJar(ClassLodingContext clLdCntx, InputStream is,
            String jarName) throws IOException
    {
        int count = 0;
        
        if (jarName == null || jarName.isEmpty())
        {
            return count;
        }
        
        String filePath = new String();
        
        filePath = _toDir;
        
        if (jarName.charAt(0) != '\\' && jarName.charAt(0) != '/')
        {
            filePath += "/";
        }
        
        filePath += jarName;
        
        File file = new File(filePath);
        
        if (!file.exists())
        {
            new File(file.getParent()).mkdirs();
            
            file.createNewFile();
            
            OutputStream os = new FileOutputStream(file);
            
            byte[] bt = new byte[2048];
            
            int len = 0;
            
            while ((len = is.read(bt)) > 0)
            {
                os.write(bt, 0, len);
            }
            
            os.close();
        }
        
        clLdCntx.getFiles().add(filePath);
        
        count++;
        
        count += clLdCntx.getCurrProcessor().processJar(filePath);
        
        return count;
    }

        
    public void addURL(URL url)
    {
        super.addURL(url);
    }
    
    /**
     * 
     */
    @SuppressWarnings("unchecked")
    protected Class findClass(String name) throws ClassNotFoundException
    {
        return super.findClass(name);
    }
    
    /**
     * 
     */
    @SuppressWarnings("unchecked")
    protected Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        return super.loadClass(name, resolve);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public Class loadClass(String name)
            throws ClassNotFoundException
    {
        return super.loadClass(name);
    }

    /**
     * 
     */
    public  URL findResource(String name) 
    {
        URL urlRes = super.findResource(name);
        return urlRes;
    }

    /**
     * 
     */
    public URL getResource(String name)
    {
        URL urlRes = super.getResource(name);
        return urlRes;
    }
    
    /**
     * 
     */
    public InputStream  getResourceAsStream(String name)
    {
        InputStream isRes = super.getResourceAsStream(name);
        return isRes;
    }
    
}
