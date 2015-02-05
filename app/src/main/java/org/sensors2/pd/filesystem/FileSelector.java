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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by thomas on 09.12.14.
 */
public class FileSelector {

	private String rootDirectory;
	private Context context;
	private TextView titleView;
	private TextView selectionView;
	private String selectedFileName = null;

	private String directory = "";
	private List<String> subDirectories = null;
	private FileSelectorListener listener = null;
	private ArrayAdapter<String> listAdapter = null;
	private boolean moveUp = false;

	public interface FileSelectorListener {
		public void onChosenFile(String filePath);
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
				String currentDirectory = directory;
				String selectedEntry = "" + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
				if (selectedEntry.charAt(selectedEntry.length() - 1) == '/')
					selectedEntry = selectedEntry.substring(0, selectedEntry.length() - 1);

				if (selectedEntry.equals("..")) {
					directory = directory.substring(0, directory.lastIndexOf("/"));
					if ("".equals(directory)) {
						directory = "/";
					}
				} else {
					directory += "/" + selectedEntry;
				}

				if ((new File(directory).isFile())) {
					directory = currentDirectory;
					selectedFileName = selectedEntry;
					selectionView.setText(directory + "/" + selectedFileName);
				} else {
					updateDirectory();
				}
			}
		}

		AlertDialog.Builder dialogBuilder = createDirectoryChooserDialog(dir, subDirectories,
				new SimpleFileDialogOnClickListener());

		dialogBuilder.setPositiveButton(R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Current directory chosen
				// Call registered listener supplied with the chosen directory
				if (listener != null) {
					listener.onChosenFile(directory + "/" + selectedFileName);
				}
			}
		}).setNegativeButton(R.string.cancel, null);

		final AlertDialog dirsDialog = dialogBuilder.create();

		// Show directory chooser dialog
		dirsDialog.show();
	}

	private boolean createSubDir(String newDir) {
		File newDirFile = new File(newDir);
		if (!newDirFile.exists()) return newDirFile.mkdir();
		else return false;
	}

	private List<String> getDirectories(String dir) {
		List<String> dirs = new ArrayList<String>();
		try {
			File dirFile = new File(dir);

			// if directory is not the base sd card directory add ".." for going up one directory
			if ((moveUp || !directory.equals(rootDirectory))
					&& !"/".equals(directory)
					) {
				dirs.add("..");
			}
			if (!dirFile.exists() || !dirFile.isDirectory()) {
				return dirs;
			}

			for (File file : dirFile.listFiles()) {
				dirs.add(file.getName());
			}
		} catch (Exception e) {
		}

		Collections.sort(dirs, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		return dirs;
	}

	private AlertDialog.Builder createDirectoryChooserDialog(String title, List<String> listItems,
															 DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		this.titleView = new TextView(context);
		this.titleView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		this.titleView.setText(R.string.open_file);

		//need to make this a variable Save as, Open, Select Directory
		this.titleView.setGravity(Gravity.CENTER_VERTICAL);
		this.titleView.setBackgroundColor(-12303292); // dark gray 	-12303292
		this.titleView.setTextColor(context.getResources().getColor(android.R.color.white));

		LinearLayout titleLayout1 = new LinearLayout(context);
		titleLayout1.setOrientation(LinearLayout.VERTICAL);
		titleLayout1.addView(this.titleView);

		LinearLayout titleLayout = new LinearLayout(context);
		titleLayout.setOrientation(LinearLayout.VERTICAL);

		selectionView = new TextView(context);
		selectionView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		selectionView.setBackgroundColor(-12303292); // dark gray -12303292
		selectionView.setTextColor(context.getResources().getColor(android.R.color.white));
		selectionView.setGravity(Gravity.CENTER_VERTICAL);
		selectionView.setText(title);

		titleLayout.addView(selectionView);

		dialogBuilder.setView(titleLayout);
		dialogBuilder.setCustomTitle(titleLayout1);
		listAdapter = createListAdapter(listItems);
		dialogBuilder.setSingleChoiceItems(listAdapter, -1, onClickListener);
		dialogBuilder.setCancelable(false);
		return dialogBuilder;
	}

	private void updateDirectory() {
		subDirectories.clear();
		subDirectories.addAll(getDirectories(directory));
		selectionView.setText(directory);
		listAdapter.notifyDataSetChanged();
	}

	private ArrayAdapter<String> createListAdapter(List<String> items) {
		return new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, android.R.id.text1, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				if (v instanceof TextView) {
					// Enable list item (directory) text wrapping
					TextView tv = (TextView) v;
					tv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
					tv.setEllipsize(null);
				}
				return v;
			}
		};
	}
}
