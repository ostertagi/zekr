# Auto-generated by EclipseNSIS Script Wizard
# Sep 9, 2005 10:48:41 PM

Name Zekr
SetCompressor /SOLID lzma

# Defines
!define REGKEY "SOFTWARE\$(^Name)"
!define APP_UNIX_NAME "zekr"
!define VERSION 0.1.0
!define COMPANY "Siahe.com"
!define URL http://siahe.com/zekr

# MUI defines
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\orange-install.ico"
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_REGISTRY_KEY Software\Zekr
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULT_FOLDER Zekr
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\orange-uninstall.ico"
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
!define MUI_LANGDLL_REGISTRY_ROOT HKLM
!define MUI_LANGDLL_REGISTRY_KEY ${REGKEY}
!define MUI_LANGDLL_REGISTRY_VALUENAME InstallerLanguage

!define MUI_WELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\orange.bmp"
!define MUI_UNWELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\orange.bmp"

# Included files
!include Sections.nsh
!include MUI.nsh

# Reserved Files
!insertmacro MUI_RESERVEFILE_LANGDLL
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

# Variables
!define BASE_APP "G:\Java\myJAVA\Zekr\dist\0.1.0\zekr0.1.0"

Var StartMenuGroup
Var JAVA_VER
Var JRE_HOME
Var JDK_HOME

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "${BASE_APP}\license.txt"
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# Installer languages
!insertmacro MUI_LANGUAGE English
!insertmacro MUI_LANGUAGE Arabic
!insertmacro MUI_LANGUAGE Farsi
!insertmacro MUI_LANGUAGE TradChinese
!insertmacro MUI_LANGUAGE Japanese
!insertmacro MUI_LANGUAGE Greek
!insertmacro MUI_LANGUAGE Albanian
!insertmacro MUI_LANGUAGE Bulgarian
!insertmacro MUI_LANGUAGE Danish
!insertmacro MUI_LANGUAGE German
!insertmacro MUI_LANGUAGE French
!insertmacro MUI_LANGUAGE Hebrew
!insertmacro MUI_LANGUAGE Indonesian
!insertmacro MUI_LANGUAGE Italian
!insertmacro MUI_LANGUAGE Korean
!insertmacro MUI_LANGUAGE Malaysian
!insertmacro MUI_LANGUAGE Dutch
!insertmacro MUI_LANGUAGE Norwegian
!insertmacro MUI_LANGUAGE Portuguese
!insertmacro MUI_LANGUAGE Romanian
!insertmacro MUI_LANGUAGE Russian
!insertmacro MUI_LANGUAGE Turkish

# Installer attributes
BrandingText "The Zekr Project"
OutFile ${APP_UNIX_NAME}-${VERSION}-setup.exe
InstallDir $PROGRAMFILES\Zekr
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion 0.1.0.0
VIAddVersionKey /lang=${LANG_ENGLISH} ProductName Zekr
VIAddVersionKey ProductVersion "${VERSION}"
VIAddVersionKey /lang=${LANG_ENGLISH} CompanyName "${COMPANY}"
VIAddVersionKey /lang=${LANG_ENGLISH} CompanyWebsite "${URL}"
VIAddVersionKey /lang=${LANG_ENGLISH} FileVersion "0.1.0"
VIAddVersionKey /lang=${LANG_ENGLISH} FileDescription "The Zekr Open Quranic Project"
VIAddVersionKey /lang=${LANG_ENGLISH} LegalCopyright "(C) 2004-2005 Mohsen Saboorian"
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails show

# Installer sections
Section -Main SEC0000
    SetOutPath $INSTDIR
    SetOverwrite on
    File /r "${BASE_APP}\*"
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
SectionEnd

Section -post SEC0001
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe

    SetOutPath "$INSTDIR\"
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
        ;Create shortcuts
        CreateDirectory "$SMPROGRAMS\Zekr"
        CreateShortCut "$SMPROGRAMS\$StartMenuGroup\Zekr.lnk" $INSTDIR\zekr.exe
        CreateShortCut "$SMPROGRAMS\$StartMenuGroup\Browse site.lnk" $INSTDIR\doc\site\index.html
        CreateShortCut "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk" $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_END

    ; javaw.exe.manifest file copy
    SetOutPath $SYSDIR
    File "${BASE_APP}\res\javaw.exe.manifest"
    SetOutPath "$JRE_HOME\bin"
    File "${BASE_APP}\res\javaw.exe.manifest"
    SetOutPath "$JDK_HOME\bin"
    File "${BASE_APP}\res\javaw.exe.manifest"

/*    ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$JAVA_VER" JavaHome
ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
StrCmp $2 "" DetectTry2
ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "MicroVersion"
StrCmp $3 "" DetectTry2
*/

    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    Goto done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o un.Main UNSEC0000
    RMDir /r /REBOOTOK $INSTDIR\zekr_
    DeleteRegValue HKLM "${REGKEY}\Components" Main
SectionEnd

Section un.post UNSEC0001
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk"
    Delete /REBOOTOK $INSTDIR\uninstall.exe
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /ifempty HKLM "${REGKEY}\Components"
    DeleteRegKey /ifempty HKLM "${REGKEY}"
    RMDir /r /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RMDir /r /REBOOTOK $INSTDIR
SectionEnd


!define GET_JAVA_URL "http://www.java.com"
Var JAVA_INSTALLATION_MSG
# Installer functions
Function .onInit
    ;Detect already running installer
    System::Call 'kernel32::CreateMutexA(i 0, i 0, t "myMutex") i .r1 ?e'
    Pop $R0
    StrCmp $R0 0 +3
    MessageBox MB_OK|MB_ICONEXCLAMATION "The installer is already running."
    Abort

    ;Detect JRE Version (should be 1.4.1+)
    call GetJavaVersion
    pop $0 ; major version
    pop $1 ; minor version
    pop $2 ; micro version

    strcmp $0 "no" JavaNotInstalled
        StrCpy $JAVA_VER "$0.$1.$2"
        IntCmp 141 "$0$1$2" FoundCorrectJavaVer FoundCorrectJavaVer JavaVerNotCorrect
    	FoundCorrectJavaVer:
            ReadRegStr $JRE_HOME HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$JAVA_VER" "JavaHome"
            ReadRegStr $JDK_HOME HKLM "SOFTWARE\JavaSoft\Java Development Kit\$JAVA_VER" "JavaHome"
;    		IfFileExists "$JAVA_HOME\bin\javaw.exe" 0 JavaNotPresent
;            MessageBox MB_OK "Found Java: $JAVA_VER at $JAVA_HOME"
    		Goto Done
        JavaVerNotCorrect:
            StrCpy $JAVA_INSTALLATION_MSG "The version of Java Runtime Environment installed on your computer is $0.$1.$2$\n.Version 1.4.1 or newer is required to run this program.$\nDo you want to browse Java download site to get it now?"
            MessageBox MB_YESNO|MB_ICONSTOP $JAVA_INSTALLATION_MSG IDNO DL_NO
            GoTo DL_YES

    JavaNotInstalled:
        MessageBox MB_YESNO|MB_ICONSTOP "Can not find any Java Runtime Environment (JRE) on this computer.$\nDo you want to browse Java download site to get it now?" IDNO DL_NO
        DL_NO:
            GoTo Abrt
        DL_YES:
            Exec '"explorer.exe" ${GET_JAVA_URL}'
    Abrt:
        Abort
    Done:

    InitPluginsDir
    Push $R1
    File /oname=$PLUGINSDIR\spltmp.bmp "${BASE_APP}\zekr-install-splash1.bmp"
    ; Delay - FadeIn - FadeOut - Keycolor - FileName
    advsplash::show 1000 700 600 -1 $PLUGINSDIR\spltmp
    Pop $R1
    Pop $R1
    !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

# Uninstaller functions
Function un.onInit
    ;Detect already running installer
    System::Call 'kernel32::CreateMutexA(i 0, i 0, t "myMutex") i .r1 ?e'
    Pop $R0
    StrCmp $R0 0 +3
    MessageBox MB_OK|MB_ICONEXCLAMATION "The installer is already running."
    Abort

    ReadRegStr $INSTDIR HKLM "${REGKEY}" Path
    ReadRegStr $StartMenuGroup HKLM "${REGKEY}" StartMenuGroup
    !insertmacro MUI_UNGETLANGUAGE
    !insertmacro SELECT_UNSECTION Main ${UNSEC0000}
FunctionEnd

Function GetJavaVersion
  push $R0
  push $R1
  push $2
  push $0
  push $3
  push $4
  
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $2 "" DetectTry2
  ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "MicroVersion"
  StrCmp $3 "" DetectTry2
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "UpdateVersion"
  StrCmp $4 "" 0 GotFromUpdate
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "JavaHome"
  Goto GotJRE
DetectTry2:
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  StrCmp $2 "" NoFound
  ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "MicroVersion"
  StrCmp $3 "" NoFound
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "UpdateVersion"
  StrCmp $4 "" 0 GotFromUpdate
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "JavaHome"
GotJRE:
  ; calc build version
  strlen $0 $3
  intcmp $0 1 0 0 GetFromMicro
  ; get it from the path
GetFromPath:
  strlen $R0 $4
  intop $R0 $R0 - 1
  StrCpy $0 ""
loopP:
  StrCpy $R1 $4 1 $R0
  StrCmp $R1 "" DotFoundP
  StrCmp $R1 "_" UScoreFound
  StrCmp $R1 "." DotFoundP
  StrCpy $0 "$R1$0"
  Goto GoLoopingP
DotFoundP:
  push ""
  Exch 6
  goto CalcMicro
UScoreFound:
  push $0
  Exch 6
  goto CalcMicro
GoLoopingP:
  intcmp $R0 0 DotFoundP DotFoundP
  IntOp $R0 $R0 - 1
  Goto loopP
GetFromMicro:
  strcpy $4 $3
  goto GetFromPath
GotFromUpdate:
  push $4
  Exch 6
 
CalcMicro:
  Push $3 ; micro
  Exch 6
  ; break version into major and minor
  StrCpy $R0 0
  StrCpy $0 ""
loop:
  StrCpy $R1 $2 1 $R0
  StrCmp $R1 "" done
  StrCmp $R1 "." DotFound
  StrCpy $0 "$0$R1"
  Goto GoLooping
DotFound:
  Push $0 ; major
  Exch 5
  StrCpy $0 ""
GoLooping:
  IntOp $R0 $R0 + 1
  Goto loop
 
done:
  Push $0 ; minor
  Exch 7
  ; restore register values
  pop $0
  pop $2
  pop $R1
  pop $R0
  pop $3
  pop $4
  return
NoFound:
  pop $4
  pop $3
  pop $0
  pop $2
  pop $R1
  pop $R0
  push ""
  push "installed"
  push "java"
  push "no"
FunctionEnd

# Installer Language Strings
# TODO Update the Language Strings with the appropriate translations.

LangString ^UninstallLink ${LANG_ENGLISH} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_ARABIC} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_FARSI} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_TRADCHINESE} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_JAPANESE} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_GREEK} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_ALBANIAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_BULGARIAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_DANISH} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_GERMAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_FRENCH} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_HEBREW} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_INDONESIAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_ITALIAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_KOREAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_MALAYSIAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_DUTCH} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_NORWEGIAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_PORTUGUESE} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_ROMANIAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_RUSSIAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_TURKISH} "Uninstall $(^Name)"
