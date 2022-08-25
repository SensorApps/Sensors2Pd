package org.sensors2.pd.filesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sensors2.pd.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by thomas on 09.12.14.
 */
public class FileSelector {

	private String rootDirectory;
	private final Context context;
	private String selectedFileName = null;

	private String directory = "";
	private List<File> subDirectories = null;
	private final FileSelectorListener listener;
	private ArrayAdapter<File> listAdapter = null;

	public interface FileSelectorListener {
		void onChosenFile(String filePath);
	}

	public FileSelector(Context context, FileSelectorListener listener) {
		this.context = context;
		this.rootDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
		this.listener = listener;

		try {
			rootDirectory = new File(rootDirectory).getCanonicalPath();
		} catch (IOException ioe) {
		}
	}

	public void chooseFile() {
		if (directory.equals("")) chooseFile(rootDirectory);
		else chooseFile(directory);
	}

	private void chooseFile(String dir) {
		File dirFile = new File(dir);
		while (!dirFile.exists() || !dirFile.isDirectory()) {
			dir = dirFile.getParent();
			assert dir != null;
			dirFile = new File(dir);
		}
		try {
			dir = new File(dir).getCanonicalPath();
		} catch (IOException ioe) {
			return;
		}

		directory = dir;
		subDirectories = getDirectories(dir);

		class SimpleFileDialogOnClickListener implements DialogInterface.OnClickListener {
			public void onClick(DialogInterface dialog, int item) {
				String dir = directory;
				String currentDirectory = dir;
				AlertDialog dirsDialog = (AlertDialog) dialog;
				String selectedEntry = "" + dirsDialog.getListView().getAdapter().getItem(item);
				if (selectedEntry.charAt(selectedEntry.length() - 1) == '/')
					selectedEntry = selectedEntry.substring(0, selectedEntry.length() - 1);

				if (selectedEntry.equals("..")) {
					dir = DirectoryUp(dir);
				} else {
					dir = selectedEntry;
				}

				if ((new File(dir).isFile())) {
					updateFile(currentDirectory, selectedEntry);
					dirsDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				} else {
					updateDirectory(dir);
					dirsDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				}
			}

			private String DirectoryUp(String directory) {
				directory = directory.substring(0, directory.lastIndexOf("/"));
				if ("".equals(directory)) {
					directory = "/";
				}
				return directory;
			}
		}

		AlertDialog.Builder dialogBuilder = createDirectoryChooserDialog(subDirectories,
				new SimpleFileDialogOnClickListener());

		dialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
			// Current directory chosen
			// Call registered listener supplied with the chosen directory
			if (listener != null) {
				listener.onChosenFile(selectedFileName);
			}
		}).setNegativeButton(R.string.cancel, null);

		final AlertDialog dirsDialog = dialogBuilder.create();
		// Show directory chooser dialog
		dirsDialog.show();
		dirsDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}

	private void updateFile(String currentDirectory, String selectedEntry) {
		directory = currentDirectory;
		selectedFileName = selectedEntry;
	}

	private void updateDirectory(String newDirectory) {
		directory = newDirectory;
		selectedFileName = null;
		subDirectories.clear();
		subDirectories.addAll(getDirectories(directory));
		listAdapter.notifyDataSetChanged();
	}

	private List<File> getDirectories(String dir) {
		List<File> dirs = new ArrayList<>();
		try {
			File dirFile = new File(dir);

			// if directory is not the base sd card directory add ".." for going up one directory
			if (!directory.equals(rootDirectory) && !"/".equals(directory)) {
				dirs.add(new File(".."));
			}
			if (!dirFile.exists() || !dirFile.isDirectory()) {
				return dirs;
			}

			dirs.addAll(Arrays.asList(dirFile.listFiles()));
		} catch (Exception e) {
		}

		Collections.sort(dirs, File::compareTo);
		return dirs;
	}

	private AlertDialog.Builder createDirectoryChooserDialog(List<File> listItems,
															 OnClickListener onClickListener) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		TextView titleView = new TextView(context);
		titleView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		titleView.setText(R.string.open_file);

		//need to make this a variable Save as, Open, Select Directory
		titleView.setGravity(Gravity.CENTER_VERTICAL);
		titleView.setBackgroundColor(context.getResources().getColor(android.R.color.background_dark));
		titleView.setTextColor(context.getResources().getColor(android.R.color.white));

		LinearLayout titleLayout1 = new LinearLayout(context);
		titleLayout1.setOrientation(LinearLayout.VERTICAL);
		titleLayout1.addView(titleView);

		LinearLayout titleLayout = new LinearLayout(context);
		titleLayout.setOrientation(LinearLayout.VERTICAL);

		dialogBuilder.setView(titleLayout);
		dialogBuilder.setCustomTitle(titleLayout1);
		listAdapter = createListAdapter(listItems);
		dialogBuilder.setSingleChoiceItems(listAdapter, -1, onClickListener);
		dialogBuilder.setCancelable(false);
		return dialogBuilder;
	}

	private ArrayAdapter<File> createListAdapter(List<File> items) {
		return new ArrayAdapter<File>(context, android.R.layout.select_dialog_item, android.R.id.text1, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				if (v instanceof TextView) {
					// Enable list item (directory) text wrapping
					TextView tv = (TextView) v;
					String fullPath = tv.getText().toString();
					tv.setText(fullPath.substring(fullPath.lastIndexOf('/') + 1));
					tv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
					tv.setEllipsize(null);
				}
				return v;
			}
		};
	}
}
