Pre-configuration:

1) Install maven. In your terminal execute the command "sudo apt-get install maven"
2) Install the Python plugin for your Eclipse. Follow these instructions carefully: http://www.vogella.com/tutorials/Python/article.html

Setup your project in Eclipse:
Whenever a path is mentioned in these instructions, your workspace is assumed as the root directory. For instance, "CsdDB/output" corresponds to "PATH/TO/YOUR/WORKSPACE/ik2200/CsdDB/output"

1) Checkout the midTerm branch (will soon merge it into develop)

2) Import the following projects
	- MininetTopology (import as "Existing projects into workspace")
	- CsdDB (import as "Existing projects into workspace")
	- ycsb (import as "Existing Maven project")

3) Generate "csd_build.xml"
	- Right click on the CsdDB project and select Export > Java > Runnable JAR file
	- Select Launch configuration "ClientMain - CsdDB"
	- Select export destination "CsdDB/output" and name to your file "CsdDBServer.jar"
	- Mark "Save as ANT script"
	- Set the "ANT script location" to CsdDB and name your file "csd_build.xml"
	- Click finish. Ignore any error/warning popups. Close the dialog and refresh your CsdDB project. 
	- You should be able to see "CsdDB/csd_build.xml" and "CsdBD/output/CsdDBServer.jar"
	- Open the generated csd_build.xml file and add the following lines after the "</jar":
		- <copy file="/home/giorgos/Documents/workspace-csd/ik2200/CsdDB/output/CsdDBServer.jar" todir="../ycsb/YCSB-master/csd/libs/"/>
		- <copy file="/home/giorgos/Documents/workspace-csd/ik2200/CsdDB/output/CsdDBServer.jar" todir="../MininetTopology/src/topology/network/server"/>
	- Replace the path of the "file=" to the appropriate one. DO NOT change the "todir" path.
	- Now every time you make changes to the CsdDB project you have to right click on the csd_build.xml file and select Run as > ANT build.
	- The above will generate two jar files. One in the ycsb project and another one in the MininetTopology project.

4) Set the path to YCSB-master
	- Open MininetTopology/src/topology/properties/ycsb_path
	- Set the right path

5) Build YCSB-master
	- If you've made changes in the elasticesearch-binding project OR IF it's the first time you build the project, you might have to clean/build YCSB-master first.
	- Open the terminal and go to ycsb/YCSB-master
	- Execute the command "mvn clean package"
