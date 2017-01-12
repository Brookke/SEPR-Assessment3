#!/usr/bin/env python3

import os.path
import PyQt5.uic
from PyQt5.QtCore import Qt
from PyQt5 import QtCore, QtWidgets, QtSql
from common import UI_SOURCE_LOCATION, SCHEMA_SCRIPT
from forms.resource import ResourceForm
from forms.costume import CostumeForm

# Load in the form for the main window
MAIN_WINDOW_SOURCE = os.path.join(UI_SOURCE_LOCATION, "form_scenario_manager_main.ui")
UiScenarioManagerMainWindow = PyQt5.uic.loadUiType(MAIN_WINDOW_SOURCE)[0]

class ScenarioManagerMainWindow(UiScenarioManagerMainWindow, QtWidgets.QMainWindow):
    """This class represents the main window of the application"""

    def __init__(self, parent = None):
        super().__init__(parent, Qt.Window)
        self.setupUi(self)

        self._database = None
        self._resource_root = "./"

        # Define all of the dependent widgets which will be used in the tab widget in this list for
        # convenient iteration.
        self._tab_widgets = [
            (ResourceForm(self.central_tab_widget), "Resources"),
            (CostumeForm(self.central_tab_widget), "Costumes"),
        ]

        # Add all of the widgets into the tab widget.
        for widget, tab_title in self._tab_widgets:
            self.central_tab_widget.addTab(widget, tab_title)

        # Disable the tab widget whilst no database is open.
        self.central_tab_widget.setEnabled(False)

    @QtCore.pyqtSlot()
    def open_database(self):
        """Open or change the working database"""

        # Define the dialogue parameters
        dialogue = QtWidgets.QFileDialog(self)
        dialogue.setAcceptMode(QtWidgets.QFileDialog.AcceptSave)
        dialogue.setFileMode(QtWidgets.QFileDialog.AnyFile)
        dialogue.setOptions(QtWidgets.QFileDialog.DontConfirmOverwrite)
        dialogue.setNameFilter("Databases (*.db)")
        dialogue.setNameFilters([
            "Databases (*.db)",
            "All Files (*.*)",
        ])

        # Get a result from the dialogue. If the user cancel, stop changing the database.
        if not dialogue.exec_():
            return
        database_name = dialogue.selectedFiles()[0]

        # Make sure the correct file extension is in the filename if we are the create the
        # database.
        if os.path.splitext(database_name)[1] != ".db" and not os.path.exists(database_name):
            database_name += ".db"

        # Open the new database.
        new_database = QtSql.QSqlDatabase.addDatabase("QSQLITE")
        new_database.setDatabaseName(database_name)
        new_database.open()

        # Check that the database opened correctly. Display an error dialogue if it didn't and
        # abort.
        if new_database.isOpenError() or not new_database.isOpen():
            msg_box = QtWidgets.QMessageBox(self)
            msg_box.setText("Error opening the database.")
            msg_box.setInformativeText(
                "There was an error opening the database. The driver gave the following error "\
                "message: \"{}\"".format(new_database.lastError().text().strip())
            )
            msg_box.setStandardButtons(QtWidgets.QMessageBox.Ok)
            msg_box.setDefaultButton(QtWidgets.QMessageBox.Ok)
            msg_box.setIcon(QtWidgets.Critical)
            msg_box.exec_()
            return

        # Swap out the old database if it exists and replace it with the new one.
        if self._database:
            self._database.close()
        self._database = new_database

        # Create the tables using an external SQL script. The script must be submitted statement
        # by statement due to API limitations where only a statement may be executed.
        with open(SCHEMA_SCRIPT, "rt") as schema_file:
            for statement in schema_file.read().split(";"):
                self._database.exec_(statement)

        # Update the depended widgets with the new database.
        for widget, _ in self._tab_widgets:
            widget.set_database(self._database)

        # The database is now open so we can enable.
        self.central_tab_widget.setEnabled(True)

    @QtCore.pyqtSlot()
    def set_resource_root(self):
        """Set the new resource root"""

        # Define the dialogue parameters
        dialogue = QtWidgets.QFileDialog(self)
        dialogue.setFileMode(QtWidgets.QFileDialog.Directory)

        # Get a result from the dialogue. If the user cancel, stop changing the root resource
        # directory.
        if not dialogue.exec_():
            return
        self._resource_root = dialogue.selectedFiles()[0]

        # Update the depended widgets with the new resource root.
        for widget, _ in self._tab_widgets:
            widget.set_resource_root(self._resource_root)
