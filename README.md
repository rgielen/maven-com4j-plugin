maven-com4j-plugin
------------------

The plugin allows you to invoke tlbimp from Maven, so you can use Maven to build Java modules to interface with COM type library.

The main enhancement over the original plugin from Jason Thrasher is the possibility to address multiple dependent typelibs at a time. This enhancement was done by Sergey Miryanov. In this particular fork, the code is enhanced to correctly support maven plugin conventions and to aim for Maven Central deployment. See below for kudos and additional references.

How to use
----------

    <plugin>
      <groupId>net.rgielen</groupId>
      <artifactId>com4j-maven-plugin</artifactId>
      <version>1.0.3-SNAPSHOT</version>
      <executions>
        <execution>
          <id>generate com.foo.* package</id>
          <goals>
            <goal>gen</goal>
          </goals>
          <configuration>
            <libraries>
              <libConfig>
                <package>com.foo.xxx</package>
                <libId>FCEB4068-E49A-416e-A1FC-45695FF937E1</libId>
              </libConfig>
              <libConfig>
                <package>com.foo.zzz</package>
                <file>zzz.dll</file>
              </libConfig>
            </libraries>
          </configuration>
        </execution>
      </executions>
    </plugin>


Disclaimer
----------

Original author of this plugin is Jason Thrasher. 

Original jar file with source code can be found here: http://download.java.net/maven/2/org/jvnet/com4j/maven-com4j-plugin/1.0/ 

This is a fork of Sergey Miryanov's fork to bring the plugin to a general audience:
https://github.com/sergey-miryanov/maven-com4j-plugin
