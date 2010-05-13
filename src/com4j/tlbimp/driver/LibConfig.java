package com4j.tlbimp.driver;
import java.io.File;

public class LibConfig
{
  /**
   * Specify the desired Java package for generated code. This can be used as
   * the alias, without the leading underscore:
   * 
   * <br/> <code>
   * &lt;package&gt;com.mycompany.com4j.someprogram&lt;/package&gt;
   * </code>
   * <br/> Or as: <br/><code>
   * &lt;_package&gt;com.mycompany.com4j.someprogram&lt;/_package&gt;
   * </code><br/>
   * 
   * The underscore is there because of the way Maven Mojos are created (the
   * pom.xml tag is the variable name), and "package" is a reserved java word
   * and can't be used as a variable.
   * 
   * @parameter expression="${package}" alias="package"
   *            default-value="org.jvnet.com4j.generated"
   */
  public String _package; // reserved keyword...

  public void setPackage (String s) 
  {
    _package = s;
  }

  /**
   * You must specify either <code>&lt;file&gt;</code> or
   * <code>&lt;libId&gt;</code>. If both are configuration parameters are
   * specified, <code>&lt;libId&gt;</code> will win, and
   * <code>&lt;file&gt;</code> will be ignored.
   * 
   * <br/> File is the Win32 program that com4j is generating the COM
   * interface for. This file must exist at the given path. The path can be
   * absolute or relative. Generally this will specify your .exe, .dll, or
   * whatever file has a Windows COM interface. <br/>
   * 
   * <code>
   * &lt;file&gt;C:\Program Files\iTunes\iTunes.exe&lt;/file&gt;
   * </code>
   * 
   * 
   * @parameter expression="${file}"
   */
  public File file;

  /**
   * You must specify either <code>&lt;file&gt;</code> or
   * <code>&lt;libId&gt;</code>. If both are configuration parameters are
   * specified, <code>&lt;libId&gt;</code> will win, and
   * <code>&lt;file&gt;</code> will be ignored.
   * 
   * <br/> LIBID is the Windows identifier of the type library to be
   * processed. It should be a string of the form
   * <code>xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx</code>. <br/>
   * 
   * Often, the location of type libraries vary from a system to system. For
   * example, the type library for Microsoft Office is installed in the same
   * directory where the user installed Microsoft Office. Because people
   * install things in different places, when multiple people are working on
   * the same project, this makes it difficult to consistently refer to the
   * same type library. libid and libver are useful in this case. Each type
   * library has a unique GUID called "LIBID", and the version of the type
   * library. <br/> For example, Microsoft Excel 2000 type library has the
   * LIBID of:<br />
   * 
   * <code>
   * &lt;libId&gt;00020813-0000-0000-C000-000000000046&lt;/libId&gt;
   * </code>
   * 
   * <br />
   * and the version of:<br />
   * 
   * <code>
   * &lt;libVer&gt;1.3&lt;/libVer&gt;
   * </code>
   * 
   * <br />
   * Together they allow you to reference a type library without knowing its
   * actual location of the file on the disk.
   * 
   * @parameter expression="${libId}"
   */
  public String libId;

  /**
   * Optional library version. Leave empty to designate the latest package
   * based on the libId. This parameter has no effect if
   * <code>&lt;libId&gt;</code> is not used.
   * 
   * @parameter expression="${libVer}"
   */
  public String libVer;
}

