ClickClick Project
==================

ClickClick is a community based extensions project for the Click Framework.

The aim of this project is to lower the barrier-to-entry on contributions 
made by Click users. Contributions can include new ``Controls``, ``Modules``, 
examples and experimental features.

Project structure
-----------------

Each module and control set has an example webapplication.

Following moduls/control sets are available:
 
 * ``[core]``              - base extensions, required by all other modules and contorls in this project
 * ``[core-examples]``     - example usage and experimental features for the above module
 * ``[jquery]``            - controls for the [jQuery library](http://jquery.com/)
 * ``[jquery-examples]``   - example usage and experimental features for jQuery controls.
 * ``[mootools]``          - controls for the [MooTools library](http://mootools.net/)
 * ``[mootools-examples]`` - example usage and experimental features for MooTools controls.

Other directories:
 
 * ``[dist]``              - build distribution directory
 * ``[documentation]``     - all documentation (javadocs too)
 * ``[lib]``               - libraries downloaded automatically with get-deps task.

Dependencies
------------
The following JARs need to be present in the ``[lib]`` directory:

 * click-core-1.5-M2.jar  
 * click-extras-1.5-M2.jar  
 * clickclick-core-0.1.jar  
 * servlet-api-2.3.jar  
 * log4j-1.2.14.jar  
 * commons-lang-2.4.jar


Building
--------
To build clickclik, execute the following steps:

 1. get all dependencies (and download manually those jars not found in Maven repo), by running:
 > ant get-deps
 2. build all modules, controls sets, examples, by running:
 > ant build-all
 3. (optional) build source packages
 > ant build-sources
 4. (optional) build the entire distribution
 > ant build-dist

------------

**Note:** for questions and suggestions, please use the Click Frameowork official mailing
lists, by prefixing the subject of your messages with the ``[ClickClick]`` tag.
