# Auto-generated by EclipseNSIS Script Wizard
# Oct 29, 2009 7:36:36 PM

Name "ذكر"
SetCompressor lzma

# General Symbol Definitions
!define REGKEY "SOFTWARE\$(^Name)"
!define APP_NAME "zekr"
!define VERSION 0.7.6.0
!define RELEASE_VERSION "0.7.6"
!define COMPANY zekr.org
!define URL http://zekr.org

# Constants
!define EXT_FILES "D:\Java\Programs\Zekr\dist\installer-files"
!define BASE_APP "D:\Java\Programs\Zekr\dist\0.7.6\final\special\win32"
#!define RECITATIONDIR "E:\recitation"

!define INSTDIR_REG_ROOT "HKLM"
!define INSTDIR_REG_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}"

# MultiUser Symbol Definitions
!define MULTIUSER_EXECUTIONLEVEL Highest
!define MULTIUSER_MUI ; force user selection installation type: all users or me only
!define MULTIUSER_INSTALLMODE_COMMANDLINE ; enables /AllUsers or /CurrentUser parameters
!define MULTIUSER_INSTALLMODE_INSTDIR Zekr
!define MULTIUSER_INSTALLMODE_INSTDIR_REGISTRY_KEY "${REGKEY}"
!define MULTIUSER_INSTALLMODE_INSTDIR_REGISTRY_VALUE "Path"

# MUI Symbol Definitions
!define MUI_ABORTWARNING
!define MUI_UNABORTWARNING
!define MUI_ICON ${EXT_FILES}\zekr-installer-icon.ico
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_REGISTRY_KEY ${REGKEY}
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULTFOLDER Zekr
!define MUI_UNICON ${EXT_FILES}\zekr-uninstaller-icon.ico
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
!define MUI_LANGDLL_REGISTRY_ROOT HKLM
!define MUI_LANGDLL_REGISTRY_KEY ${REGKEY}
!define MUI_LANGDLL_REGISTRY_VALUENAME InstallerLanguage
!define MUI_WELCOMEFINISHPAGE_BITMAP ${EXT_FILES}\zekr-installer-image.bmp
!define MUI_UNWELCOMEFINISHPAGE_BITMAP ${EXT_FILES}\zekr-uninstaller-image.bmp
!define MUI_LANGDLL_ALWAYSSHOW
; cleanup functions
;!define MUI_CUSTOMFUNCTION_ABORT cleanUp
;!define MUI_CUSTOMFUNCTION_UNABORT cleanUp

# Included files
!include MultiUser.nsh
!include Sections.nsh
!include MUI2.nsh
;!include AdvUninstLog.nsh ; makes installer non-responsive for recitation files. 
!include Sections.nsh
!include StrFunc.nsh

; AdvUninstLog doesn't work if not set
; INTERACTIVE_UNINSTALL or UNATTENDED_UNINSTALL
;!insertmacro UNATTENDED_UNINSTALL

# Reserved Files
!insertmacro MUI_RESERVEFILE_LANGDLL
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

# Variables
Var StartMenuGroup

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE ${EXT_FILES}\zekr-license.txt
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# These indented statements modify settings for MUI_PAGE_FINISH
!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_NOTCHECKED
!define MUI_FINISHPAGE_RUN_TEXT "$(LAUNCH_APP)"
!define MUI_FINISHPAGE_RUN_FUNCTION "launchZekr"
!define MUI_FINISHPAGE_SHOWREADME_NOTCHECKED
!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\readme.txt

!insertmacro MUI_PAGE_FINISH

# Installer languages
!insertmacro MUI_LANGUAGE English
!insertmacro MUI_LANGUAGE Farsi
!insertmacro MUI_LANGUAGE Arabic

# Installer attributes
BrandingText "The Zekr Project"
;OutFile ${APP_NAME}-${RELEASE_VERSION}-setup.exe
OutFile ${APP_NAME}-setup.exe
InstallDir $PROGRAMFILES\Zekr
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion ${VERSION}
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductName "Zekr"
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyName "${COMPANY}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyWebsite "${URL}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileVersion "${RELEASE_VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileDescription "Zekr - Open Qur'anic Project"
VIAddVersionKey /LANG=${LANG_ENGLISH} LegalCopyright "(C) 2004-2010 zekr.org"
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails show

# Installer sections
Section $(SEC1_NAME) SEC0000
	SetOutPath $INSTDIR
    ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
	SetOverwrite on
	File /r "${BASE_APP}\*"

    Call "createInstallDirFile"

	SetOverwrite try
    File "/oname=$FONTS\ScheherazadeRegOT.ttf" ${EXT_FILES}\ScheherazadeRegOT.ttf
    File "/oname=$FONTS\me_quran_volt_newmet.ttf" ${EXT_FILES}\me_quran_volt_newmet.ttf
    File "/oname=$FONTS\UthmanTN1_Ver07.otf" ${EXT_FILES}\UthmanTN1_Ver07.otf
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
    ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
SectionEnd

SectionGroup /e $(SEC2_NAME) SECGRP0000
    Section /o $(SEC21_NAME) SEC0001
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        CreateDirectory $INSTDIR\res\audio\mansouri-48kbps-offline
        CopyFiles "$EXEDIR\recitation\mansouri-48kbps-offline\*" "$INSTDIR\res\audio\mansouri-48kbps-offline" 614794
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\mansouri-48kbps-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Mansouri 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section /o $(SEC22_NAME) SEC0002
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        CreateDirectory $INSTDIR\res\audio\muaiqly-48kbps-offline
        CopyFiles "$EXEDIR\recitation\muaiqly-48kbps-offline\*" "$INSTDIR\res\audio\muaiqly-48kbps-offline" 444845
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\muaiqly-48kbps-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Al-Muaiqly 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section $(SEC23_NAME) SEC0003
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        CreateDirectory $INSTDIR\res\audio\afasy-40kbps-offline
        CopyFiles "$EXEDIR\recitation\afasy-40kbps-offline\*" "$INSTDIR\res\audio\afasy-40kbps-offline" 520734
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\afasy-40kbps-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Al-Afasy 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section /o $(SEC24_NAME) SEC0004
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        CreateDirectory $INSTDIR\res\audio\ghamdi-40kbps-offline
        CopyFiles "$EXEDIR\recitation\ghamdi-40kbps-offline\*" "$INSTDIR\res\audio\ghamdi-40kbps-offline" 435137
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\ghamdi-40kbps-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Al-Ghamdi 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section /o $(SEC25_NAME) SEC0005
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        CreateDirectory $INSTDIR\res\audio\shatri-48kbps-offline
        CopyFiles "$EXEDIR\recitation\shatri-48kbps-offline\*" "$INSTDIR\res\audio\shatri-48kbps-offline" 547431
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\shatri-48kbps-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Ash-Shatri 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section /o $(SEC26_NAME) SEC0006
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        CreateDirectory $INSTDIR\res\audio\abdulbasit-32kbps-offline
        CopyFiles "$EXEDIR\recitation\abdulbasit-32kbps-offline\*" "$INSTDIR\res\audio\abdulbasit-32kbps-offline" 840474
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\abdulbasit-32kbps-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Abdulbasit 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd
SectionGroupEnd

SectionGroup /e $(SEC3_NAME) SECGRP0001
    Section $(SEC31_NAME) SEC0011
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\mansouri-48kbps-dvd-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Mansouri-dvd 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section $(SEC32_NAME) SEC0012
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\muaiqly-48kbps-dvd-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Al-Muaiqly-dvd 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section /o $(SEC33_NAME) SEC0013
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\afasy-40kbps-dvd-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Al-Afasy-dvd 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section $(SEC34_NAME) SEC0014
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\ghamdi-40kbps-dvd-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Al-Ghamdi-dvd 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section $(SEC35_NAME) SEC0015
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\shatri-48kbps-dvd-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Ash-Shatri-dvd 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd

    Section $(SEC36_NAME) SEC0016
        ;!insertmacro UNINSTALL.LOG_OPEN_INSTALL
        SetOutPath $INSTDIR
        SetOverwrite on
        SetOutPath $INSTDIR\res\audio
        File /nonfatal E:\recitation\abdulbasit-32kbps-dvd-offline.properties
        WriteRegStr HKLM "${REGKEY}\Components" Abdulbasit-dvd 1
        ;!insertmacro UNINSTALL.LOG_CLOSE_INSTALL
    SectionEnd
SectionGroupEnd

Section -post SEC_MENU
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    SetOutPath $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\Zekr.lnk" $INSTDIR\zekr.exe
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk" $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_END
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
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o -un.$(SEC36_NAME) UNSEC0016
    Delete /REBOOTOK $INSTDIR\res\audio\abdulbasit-32kbps-dvd-offline.properties
    DeleteRegValue HKLM "${REGKEY}\Components" Abdulbasit-dvd
SectionEnd

Section /o -un.$(SEC35_NAME) UNSEC0015
    Delete /REBOOTOK $INSTDIR\res\audio\shatri-48kbps-dvd-offline.properties
    DeleteRegValue HKLM "${REGKEY}\Components" Ash-Shatri-dvd
SectionEnd

Section /o -un.$(SEC34_NAME) UNSEC0014
    Delete /REBOOTOK $INSTDIR\res\audio\ghamdi-40kbps-dvd-offline.properties
    DeleteRegValue HKLM "${REGKEY}\Components" Al-Ghamdi-dvd
SectionEnd

Section /o -un.$(SEC33_NAME) UNSEC0013
    Delete /REBOOTOK $INSTDIR\res\audio\afasy-40kbps-dvd-offline.properties
    DeleteRegValue HKLM "${REGKEY}\Components" Al-Afasy-dvd
SectionEnd

Section /o -un.$(SEC32_NAME) UNSEC0012
    Delete /REBOOTOK $INSTDIR\res\audio\muaiqly-48kbps-dvd-offline.properties
    DeleteRegValue HKLM "${REGKEY}\Components" Al-Muaiqly-dvd
SectionEnd

Section /o -un.$(SEC31_NAME) UNSEC0011
    Delete /REBOOTOK $INSTDIR\res\audio\mansouri-48kbps-dvd-offline.properties
    DeleteRegValue HKLM "${REGKEY}\Components" Mansouri-dvd
SectionEnd

# Uninstaller sections
Section /o -un.$(SEC26_NAME) UNSEC0006
    Delete /REBOOTOK $INSTDIR\res\audio\abdulbasit-32kbps-offline.properties
    RmDir /r /REBOOTOK $INSTDIR\res\audio\abdulbasit-32kbps-offline
    DeleteRegValue HKLM "${REGKEY}\Components" Abdulbasit
SectionEnd

Section /o -un.$(SEC25_NAME) UNSEC0005
    Delete /REBOOTOK $INSTDIR\res\audio\shatri-48kbps-offline.properties
    RmDir /r /REBOOTOK $INSTDIR\res\audio\shatri-48kbps-offline
    DeleteRegValue HKLM "${REGKEY}\Components" Ash-Shatri
SectionEnd

Section /o -un.$(SEC24_NAME) UNSEC0004
    Delete /REBOOTOK $INSTDIR\res\audio\ghamdi-40kbps-offline.properties
    RmDir /r /REBOOTOK $INSTDIR\res\audio\ghamdi-40kbps-offline
    DeleteRegValue HKLM "${REGKEY}\Components" Al-Ghamdi
SectionEnd

Section /o -un.$(SEC23_NAME) UNSEC0003
    Delete /REBOOTOK $INSTDIR\res\audio\afasy-40kbps-offline.properties
    RmDir /r /REBOOTOK $INSTDIR\res\audio\afasy-40kbps-offline
    DeleteRegValue HKLM "${REGKEY}\Components" Al-Afasy
SectionEnd

Section /o -un.$(SEC22_NAME) UNSEC0002
    Delete /REBOOTOK $INSTDIR\res\audio\muaiqly-48kbps-offline.properties
    RmDir /r /REBOOTOK $INSTDIR\res\audio\muaiqly-48kbps-offline
    DeleteRegValue HKLM "${REGKEY}\Components" Al-Muaiqly
SectionEnd

Section /o -un.$(SEC21_NAME) UNSEC0001
    Delete /REBOOTOK $INSTDIR\res\audio\mansouri-48kbps-offline.properties
    RmDir /r /REBOOTOK $INSTDIR\res\audio\mansouri-48kbps-offline
    DeleteRegValue HKLM "${REGKEY}\Components" Mansouri
SectionEnd

Section /o -un.$(SEC1_NAME) UNSEC0000
    ;Delete /REBOOTOK $FONTS\UthmanTN1_Ver07.otf
    ;Delete /REBOOTOK $FONTS\me_quran_volt_newmet.ttf
    ;Delete /REBOOTOK $FONTS\ScheherazadeRegOT.ttf

    ;begin uninstall, especially for MUI could be added in UN.onInit function instead
    ;!insertmacro UNINSTALL.LOG_BEGIN_UNINSTALL
    ;uninstall from path, must be repeated for every install logged path individual
    ;!insertmacro UNINSTALL.LOG_UNINSTALL "$INSTDIR"
    ;uninstall from path, must be repeated for every install logged path individual
    ;!insertmacro UNINSTALL.LOG_UNINSTALL "$APPDATA\${APP_NAME}"
    ;end uninstall, after uninstall from all logged paths has been performed
    ;!insertmacro UNINSTALL.LOG_END_UNINSTALL

    DeleteRegValue HKLM "${REGKEY}\Components" Main
SectionEnd

Section -un.post UNSEC_MENU
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk"
    Delete /REBOOTOK $INSTDIR\uninstall.exe
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    RmDir /r /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RmDir /r /REBOOTOK $INSTDIR
SectionEnd

Function createInstallDirFile
    Push "$EXEDIR$\r$\n"
    Push "\"
    Push "/"
    Call StrRep
    Pop "$R0"

    CreateDirectory $INSTDIR\res\config
    FileOpen $9 "$INSTDIR\res\config\install.properties" w ;Open an empty file and fill it
    FileWrite $9 "zekr.install.dir = $R0"
    FileClose $9 ;Closes the filled file
    Return
FunctionEnd

Function launchZekr
    ExecShell "" "$INSTDIR\zekr.exe"
FunctionEnd

Function .onInstSuccess
    ;create/update log always within .onInstSuccess function
    ;!insertmacro UNINSTALL.LOG_UPDATE_INSTALL
FunctionEnd

# Installer functions
Function .onInit
    ;Detect already running installer
    System::Call 'kernel32::CreateMutexA(i 0, i 0, t "myMutex") i .r1 ?e'
    Pop $R0
    StrCmp $R0 0 +3
    MessageBox MB_OK|MB_ICONEXCLAMATION "The installer is already running."
    Abort

    InitPluginsDir
    Push $R1
    File /oname=$PLUGINSDIR\spltmp.bmp ${EXT_FILES}\zekr-installer-splash.bmp
    advsplash::show 1200 700 500 -1 $PLUGINSDIR\spltmp
    Pop $R1
    Pop $R1
    !insertmacro MUI_LANGDLL_DISPLAY
    !insertmacro MULTIUSER_INIT

    ;prepare log always within .onInit function
    ;!insertmacro UNINSTALL.LOG_PREPARE_INSTALL

    ; Make main section mandatory (selected and disabled section checkbox)
	SectionSetFlags ${SEC0000} 17

    StrCpy $1 ${SEC0001} ; Group 1 - Section 1 is selected by default
    StrCpy $2 ${SEC0002} ; Group 1 - Section 2 is selected by default
    StrCpy $3 ${SEC0003} ; Group 1 - Section 3 is selected by default
    StrCpy $4 ${SEC0004} ; Group 1 - Section 4 is selected by default
    StrCpy $5 ${SEC0005} ; Group 1 - Section 5 is selected by default
    StrCpy $6 ${SEC0006} ; Group 1 - Section 6 is selected by default
FunctionEnd

Function .onSelChange
    !insertmacro StartRadioButtons $1
        !insertmacro RadioButton ${SEC0001}
        !insertmacro RadioButton ${SEC0011}
    !insertmacro EndRadioButtons
    !insertmacro StartRadioButtons $2
        !insertmacro RadioButton ${SEC0002}
        !insertmacro RadioButton ${SEC0012}
    !insertmacro EndRadioButtons
    !insertmacro StartRadioButtons $3
        !insertmacro RadioButton ${SEC0003}
        !insertmacro RadioButton ${SEC0013}
    !insertmacro EndRadioButtons
    !insertmacro StartRadioButtons $4
        !insertmacro RadioButton ${SEC0004}
        !insertmacro RadioButton ${SEC0014}
    !insertmacro EndRadioButtons
    !insertmacro StartRadioButtons $5
        !insertmacro RadioButton ${SEC0005}
        !insertmacro RadioButton ${SEC0015}
    !insertmacro EndRadioButtons
    !insertmacro StartRadioButtons $6
        !insertmacro RadioButton ${SEC0006}
        !insertmacro RadioButton ${SEC0016}
    !insertmacro EndRadioButtons
FunctionEnd

# Uninstaller functions
Function un.onInit
    ;Detect already running installer
    System::Call 'kernel32::CreateMutexA(i 0, i 0, t "myMutex") i .r1 ?e'
    Pop $R0
    StrCmp $R0 0 +3
    MessageBox MB_OK|MB_ICONEXCLAMATION "The uninstaller is already running."
    Abort

    !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuGroup
    !insertmacro MUI_UNGETLANGUAGE
    !insertmacro MULTIUSER_UNINIT
    !insertmacro SELECT_UNSECTION $(SEC1_NAME) ${UNSEC0000}
    !insertmacro SELECT_UNSECTION $(SEC21_NAME) ${UNSEC0001}
    !insertmacro SELECT_UNSECTION $(SEC22_NAME) ${UNSEC0002}
    !insertmacro SELECT_UNSECTION $(SEC23_NAME) ${UNSEC0003}
    !insertmacro SELECT_UNSECTION $(SEC24_NAME) ${UNSEC0004}
    !insertmacro SELECT_UNSECTION $(SEC25_NAME) ${UNSEC0005}
    !insertmacro SELECT_UNSECTION $(SEC26_NAME) ${UNSEC0006}
    !insertmacro SELECT_UNSECTION $(SEC31_NAME) ${UNSEC0011}
    !insertmacro SELECT_UNSECTION $(SEC32_NAME) ${UNSEC0012}
    !insertmacro SELECT_UNSECTION $(SEC33_NAME) ${UNSEC0013}
    !insertmacro SELECT_UNSECTION $(SEC34_NAME) ${UNSEC0014}
    !insertmacro SELECT_UNSECTION $(SEC35_NAME) ${UNSEC0015}
    !insertmacro SELECT_UNSECTION $(SEC36_NAME) ${UNSEC0016}
FunctionEnd

# Section Descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0000} $(SEC0000_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SECGRP0000} $(SECGRP0000_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0001} $(SEC0001_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0002} $(SEC0002_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0003} $(SEC0003_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0004} $(SEC0004_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0005} $(SEC0005_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0006} $(SEC0006_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SECGRP0001} $(SECGRP0001_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0011} $(SEC0011_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0012} $(SEC0012_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0013} $(SEC0013_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0014} $(SEC0014_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0015} $(SEC0015_DESC)
    !insertmacro MUI_DESCRIPTION_TEXT ${SEC0016} $(SEC0016_DESC)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

# Installer Language Strings
# TODO Update the Language Strings with the appropriate translations.

LangString ^UninstallLink ${LANG_FARSI} "حذف $(^Name)"
LangString ^UninstallLink ${LANG_ARABIC} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_ENGLISH} "Uninstall $(^Name)"

LangString LAUNCH_APP ${LANG_FARSI} "اجرای برنامه‌ی ذکر"
LangString LAUNCH_APP ${LANG_ENGLISH} "Launch Zekr"
LangString LAUNCH_APP ${LANG_ARABIC} "Launch Zekr"

LangString SEC1_NAME ${LANG_FARSI} "اصلی"
LangString SEC2_NAME ${LANG_FARSI} "ترتیل قرآن"
LangString SEC21_NAME ${LANG_FARSI} "منصوری"
LangString SEC22_NAME ${LANG_FARSI} "معیقلی"
LangString SEC23_NAME ${LANG_FARSI} "عفاسی"
LangString SEC24_NAME ${LANG_FARSI} "غامدی"
LangString SEC25_NAME ${LANG_FARSI} "شاطری"
LangString SEC26_NAME ${LANG_FARSI} "عبدالباسط"
LangString SEC3_NAME ${LANG_FARSI} "ترتیل قرآن از روی دی‌وی‌دی"
LangString SEC31_NAME ${LANG_FARSI} "منصوری-دی‌وی‌دی"
LangString SEC32_NAME ${LANG_FARSI} "معیقلی-دی‌وی‌دی"
LangString SEC33_NAME ${LANG_FARSI} "عفاسی-دی‌وی‌دی"
LangString SEC34_NAME ${LANG_FARSI} "غامدی-دی‌وی‌دی"
LangString SEC35_NAME ${LANG_FARSI} "شاطری-دی‌وی‌دی"
LangString SEC36_NAME ${LANG_FARSI} "عبدالباسط-دی‌وی‌دی"

LangString SEC1_NAME ${LANG_ENGLISH} "Main"
LangString SEC2_NAME ${LANG_ENGLISH} "Qur'an Recitations"
LangString SEC21_NAME ${LANG_ENGLISH} "Mansouri"
LangString SEC22_NAME ${LANG_ENGLISH} "Al-Muaiqly"
LangString SEC23_NAME ${LANG_ENGLISH} "Al-Afasy"
LangString SEC24_NAME ${LANG_ENGLISH} "Al-Ghamdi"
LangString SEC25_NAME ${LANG_ENGLISH} "Ash-Shatri"
LangString SEC26_NAME ${LANG_ENGLISH} "Abdulbasit"
LangString SEC3_NAME ${LANG_ENGLISH} "Qur'an Recitations (play from DVD)"
LangString SEC31_NAME ${LANG_ENGLISH} "Mansouri (DVD)"
LangString SEC32_NAME ${LANG_ENGLISH} "Al-Muaiqly (DVD)"
LangString SEC33_NAME ${LANG_ENGLISH} "Al-Afasy (DVD)"
LangString SEC34_NAME ${LANG_ENGLISH} "Al-Ghamdi (DVD)"
LangString SEC35_NAME ${LANG_ENGLISH} "Ash-Shatri (DVD)"
LangString SEC36_NAME ${LANG_ENGLISH} "Abdulbasit (DVD)"

LangString SEC1_NAME ${LANG_ARABIC} "Main"
LangString SEC2_NAME ${LANG_ARABIC} "Qur'an Recitations"
LangString SEC21_NAME ${LANG_ARABIC} "Mansouri"
LangString SEC22_NAME ${LANG_ARABIC} "Al-Muaiqly"
LangString SEC23_NAME ${LANG_ARABIC} "Al-Afasy"
LangString SEC24_NAME ${LANG_ARABIC} "Al-Ghamdi"
LangString SEC25_NAME ${LANG_ARABIC} "Ash-Shatri"
LangString SEC26_NAME ${LANG_ARABIC} "Abdulbasit"
LangString SEC3_NAME ${LANG_ARABIC} "Qur'an Recitations (play from DVD)"
LangString SEC31_NAME ${LANG_ARABIC} "Mansouri (DVD)"
LangString SEC32_NAME ${LANG_ARABIC} "Al-Muaiqly (DVD)"
LangString SEC33_NAME ${LANG_ARABIC} "Al-Afasy (DVD)"
LangString SEC34_NAME ${LANG_ARABIC} "Al-Ghamdi (DVD)"
LangString SEC35_NAME ${LANG_ARABIC} "Ash-Shatri (DVD)"
LangString SEC36_NAME ${LANG_ARABIC} "Abdulbasit (DVD)"

LangString SEC0000_DESC ${LANG_FARSI} "فایل‌های اصلی برنامه"
LangString SECGRP0000_DESC ${LANG_FARSI} "نصب ترتیل قرآن؛ فایل‌های ترتیل به‌طور کامل بر روی هارد دیسک نصب می‌شوند"
LangString SEC0001_DESC ${LANG_FARSI} "ترتیل کامل قرآن با صدای کریم منصوری"
LangString SEC0002_DESC ${LANG_FARSI} "ترتیل کامل قرآن با صدای ماهر معیقلی"
LangString SEC0003_DESC ${LANG_FARSI} "ترتیل کامل قرآن با صدای مشاری بن راشد عفاسی"
LangString SEC0004_DESC ${LANG_FARSI} "ترتیل کامل قرآن با صدای سعد غامدی"
LangString SEC0005_DESC ${LANG_FARSI} "ترتیل کامل قرآن با صدای ابوبکر شاطری"
LangString SEC0006_DESC ${LANG_FARSI} "ترتیل کامل قرآن با صدای عبدالباسط محمد عبدالصمد"
LangString SECGRP0001_DESC ${LANG_FARSI} "نصب ترتیل قرآن؛ برای پخش قرائت باید دی‌وی‌دی در درایو باشد"
LangString SEC0011_DESC ${LANG_FARSI} "ترتیل کامل قرآن از روی دی‌وی‌دی با صدای کریم منصوری"
LangString SEC0012_DESC ${LANG_FARSI} "ترتیل کامل قرآن از روی دی‌وی‌دی با صدای ماهر معیقلی"
LangString SEC0013_DESC ${LANG_FARSI} "ترتیل کامل قرآن از روی دی‌وی‌دی با صدای مشاری بن راشد عفاسی"
LangString SEC0014_DESC ${LANG_FARSI} "ترتیل کامل قرآن از روی دی‌وی‌دی با صدای سعد غامدی"
LangString SEC0015_DESC ${LANG_FARSI} "ترتیل کامل قرآن از روی دی‌وی‌دی با صدای ابوبکر شاطری"
LangString SEC0016_DESC ${LANG_FARSI} "ترتیل کامل قرآن از روی دی‌وی‌دی با صدای عبدالباسط محمد عبدالصمد"

LangString SEC0000_DESC ${LANG_ENGLISH} "Main installation files"
LangString SECGRP0000_DESC ${LANG_ENGLISH} "Install recitations; Recitation files are fully copied to your hard disk"
LangString SEC0001_DESC ${LANG_ENGLISH} "Complete Qur'an recitation by Karim Mansouri"
LangString SEC0002_DESC ${LANG_ENGLISH} "Complete Qur'an recitation by Mahir Al-Muaiqly"
LangString SEC0003_DESC ${LANG_ENGLISH} "Complete Qur'an recitation by Mishary Al-Afasy"
LangString SEC0004_DESC ${LANG_ENGLISH} "Complete Qur'an recitation by Saad Al-Ghamdi"
LangString SEC0005_DESC ${LANG_ENGLISH} "Complete Qur'an recitation by Abu Bakr Ash-Shatri"
LangString SEC0006_DESC ${LANG_ENGLISH} "Complete Qur'an recitation by Abdulbasit Muhammad Abdussamad"
LangString SECGRP0001_DESC ${LANG_ENGLISH} "Install recitations (Zekr DVD must be in your drive in order to play these recitations)"
LangString SEC0011_DESC ${LANG_ENGLISH} "Complete Qur'an recitation from DVD by Karim Mansouri"
LangString SEC0012_DESC ${LANG_ENGLISH} "Complete Qur'an recitation from DVD by Mahir Al-Muaiqly"
LangString SEC0013_DESC ${LANG_ENGLISH} "Complete Qur'an recitation from DVD by Mishary Al-Afasy"
LangString SEC0014_DESC ${LANG_ENGLISH} "Complete Qur'an recitation from DVD by Saad Al-Ghamdi"
LangString SEC0015_DESC ${LANG_ENGLISH} "Complete Qur'an recitation from DVD by Abu Bakr Ash-Shatri"
LangString SEC0016_DESC ${LANG_ENGLISH} "Complete Qur'an recitation from DVD by Abdulbasit Muhammad Abdussamad"

LangString SEC0000_DESC ${LANG_ARABIC} "Main installation files"
LangString SECGRP0000_DESC ${LANG_ARABIC} "Install recitations; Recitation files are fully copied to your hard disk"
LangString SEC0001_DESC ${LANG_ARABIC} "Complete Qur'an recitation by Karim Mansouri"
LangString SEC0002_DESC ${LANG_ARABIC} "Complete Qur'an recitation by Mahir Al-Muaiqly"
LangString SEC0003_DESC ${LANG_ARABIC} "Complete Qur'an recitation by Mishary Al-Afasy"
LangString SEC0004_DESC ${LANG_ARABIC} "Complete Qur'an recitation by Saad Al-Ghamdi"
LangString SEC0005_DESC ${LANG_ARABIC} "Complete Qur'an recitation by Abu Bakr Ash-Shatri"
LangString SEC0006_DESC ${LANG_ARABIC} "Complete Qur'an recitation by Abdulbasit Muhammad Abdussamad"
LangString SECGRP0001_DESC ${LANG_ARABIC} "Install recitations (Zekr DVD must be in your drive in order to play these recitations)"
LangString SEC0011_DESC ${LANG_ARABIC} "Complete Qur'an recitation from DVD by Karim Mansouri"
LangString SEC0012_DESC ${LANG_ARABIC} "Complete Qur'an recitation from DVD by Mahir Al-Muaiqly"
LangString SEC0013_DESC ${LANG_ARABIC} "Complete Qur'an recitation from DVD by Mishary Al-Afasy"
LangString SEC0014_DESC ${LANG_ARABIC} "Complete Qur'an recitation from DVD by Saad Al-Ghamdi"
LangString SEC0015_DESC ${LANG_ARABIC} "Complete Qur'an recitation from DVD by Abu Bakr Ash-Shatri"
LangString SEC0016_DESC ${LANG_ARABIC} "Complete Qur'an recitation from DVD by Abdulbasit Muhammad Abdussamad"

Function StrRep
  Exch $R4 ; $R4 = Replacement String
  Exch
  Exch $R3 ; $R3 = String to replace (needle)
  Exch 2
  Exch $R1 ; $R1 = String to do replacement in (haystack)
  Push $R2 ; Replaced haystack
  Push $R5 ; Len (needle)
  Push $R6 ; len (haystack)
  Push $R7 ; Scratch reg
  StrCpy $R2 ""
  StrLen $R5 $R3
  StrLen $R6 $R1
loop:
  StrCpy $R7 $R1 $R5
  StrCmp $R7 $R3 found
  StrCpy $R7 $R1 1 ; - optimization can be removed if U know len needle=1
  StrCpy $R2 "$R2$R7"
  StrCpy $R1 $R1 $R6 1
  StrCmp $R1 "" done loop
found:
  StrCpy $R2 "$R2$R4"
  StrCpy $R1 $R1 $R6 $R5
  StrCmp $R1 "" done loop
done:
  StrCpy $R3 $R2
  Pop $R7
  Pop $R6
  Pop $R5
  Pop $R2
  Pop $R1
  Pop $R4
  Exch $R3
FunctionEnd
