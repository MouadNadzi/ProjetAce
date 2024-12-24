import 'dart:convert';
import 'package:flutter/material.dart';
import 'dart:io'; // Import dart:io for File operations
import 'package:flutter_svg/flutter_svg.dart'; // 1. Import flutter_svg

import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:archive/archive_io.dart';
import 'package:path_provider/path_provider.dart';
import 'package:file_picker/file_picker.dart';
import 'package:simple_icons/simple_icons.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Code Generator',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: CodeGenerator(),
    );
  }
}

class Alert extends StatelessWidget {
  final String message;
  final String type;
  final VoidCallback onClose;

  Alert({required this.message, this.type = 'success', required this.onClose});

  @override
  Widget build(BuildContext context) {
    return Positioned(
      bottom: 16.0,
      right: 16.0,
      child: Material(
        elevation: 8.0,
        borderRadius: BorderRadius.circular(8.0),
        color: type == 'success' ? Colors.green[100] : Colors.red[100],
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                message,
                style: TextStyle(
                  color: type == 'success' ? Colors.green[800] : Colors.red[800],
                ),
              ),
              SizedBox(width: 16.0),
              InkWell(
                onTap: onClose,
                child: Text(
                  '✕',
                  style: TextStyle(
                    color: Colors.grey[700],
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class FileTree extends StatefulWidget {
  final Map<String, String> files;

  FileTree({required this.files});

  @override
  _FileTreeState createState() => _FileTreeState();
}

class _FileTreeState extends State<FileTree> {
  Map<String, bool> expanded = {};

  void toggleFolder(String path) {
    setState(() {
      expanded[path] = !expanded[path]!;
    });
  }

  Widget renderTree({String path = '', int level = 0}) {
    List<String> items = widget.files.keys.where((key) {
      List<String> keyParts = key.split('/');
      List<String> pathParts = path.isEmpty ? [] : path.split('/');
      return key.startsWith(path) && keyParts.length == pathParts.length + (path.isEmpty ? 0 : 1);
    }).toList();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: items.map((item) {
        bool isFolder = widget.files.keys.any((key) => key.startsWith('$item/') && key != item);
        bool isExpanded = expanded[item] ?? false;

        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            InkWell(
              onTap: isFolder ? () => toggleFolder(item) : null,
              child: Padding(
                padding: EdgeInsets.only(left: level * 20.0, top: 4.0, bottom: 4.0),
                child: Row(
                  children: [
                    isFolder
                        ? Icon(isExpanded ? Icons.arrow_drop_down : Icons.arrow_right)
                        : SizedBox(width: 24.0),
                    isFolder
                        ? Icon(Icons.folder, color: Colors.blue[500])
                        : Icon(Icons.insert_drive_file, color: Colors.grey[500]),
                    SizedBox(width: 8.0),
                    Text(item.split('/').last),
                  ],
                ),
              ),
            ),
            if (isFolder && isExpanded)
              renderTree(path: '$item/', level: level + 1),
          ],
        );
      }).toList(),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 16.0),
      child: renderTree(),
    );
  }
}

class CodeGenerator extends StatefulWidget {
  @override
  _CodeGeneratorState createState() => _CodeGeneratorState();
}

class _CodeGeneratorState extends State<CodeGenerator> {
  String platform = 'android';
  String pattern = 'mvc';
  String projectName = '';
  String packageName = '';
  int activeTab = 0;
  Map<String, dynamic>? generatedProject;
  Map<String, dynamic>? alert;

  List<Map<String, String>> patterns = [
    {'value': 'mvc', 'label': 'MVC'},
    {'value': 'mvvm', 'label': 'MVVM'}
  ];

  void showAlert(String message, {String type = 'success'}) {
    setState(() {
      alert = {'message': message, 'type': type};
    });
    Future.delayed(Duration(seconds: 3), () {
      setState(() {
        alert = null;
      });
    });
  }

  Future<void> handleGenerate() async {
    if (projectName.isEmpty || packageName.isEmpty) {
      showAlert('Project name and package name are required', type: 'error');
      return;
    }

    try {
      final response = await http.post(
        Uri.parse('http://localhost:8080/api/generator/generate'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode({
          'platform': platform,
          'pattern': pattern,
          'projectName': projectName,
          'packageName': packageName
        }),
      );

      if (response.statusCode != 200) {
        throw Exception('HTTP error! status: ${response.statusCode}');
      }

      final data = json.decode(response.body);
      setState(() {
        generatedProject = data;
        activeTab = 1;
      });
      showAlert('Code generated successfully!');
    } catch (error) {
      print('Generation error: $error');
      showAlert('Failed to generate code. Please try again.', type: 'error');
    }
  }

  Future<void> handleDownload() async {
    if (generatedProject == null) return;

    try {
      var encoder = ZipFileEncoder();
      String dir = (await getApplicationDocumentsDirectory()).path;
      encoder.create('$dir/$projectName-$pattern-project.zip');

      generatedProject!['projectFiles'].forEach((path, content) {
        encoder.addFile(File('$dir/$path')..writeAsStringSync(content));
      });

      encoder.close();

      // Use file_picker to let user choose save location
      String? outputFile = await FilePicker.platform.saveFile(
        dialogTitle: 'Please select an output file:',
        fileName: '$projectName-$pattern-project.zip',
      );

      if (outputFile == null) {
        // User canceled the picker
        return;
      }

      File savedFile = File(outputFile);
      await savedFile.writeAsBytes(File('$dir/$projectName-$pattern-project.zip').readAsBytesSync());

      showAlert('Project downloaded successfully!');
    } catch (error) {
      print('Download error: $error');
      showAlert('Failed to download project files', type: 'error');
    }
  }

  

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Code Generator'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Card(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  children: [
                    Text(
                      'Code Generator',
                      style: TextStyle(
                        fontSize: 24.0,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    SizedBox(height: 4.0),
                    Text(
                      'Generate code using design patterns',
                      style: TextStyle(
                        fontSize: 16.0,
                        color: Colors.grey[500],
                      ),
                    ),
                  ],
                ),
              ),
              DefaultTabController(
                length: 3,
                initialIndex: activeTab,
                child: Expanded(
                  child: Column(
                    children: [
                      TabBar(
                        labelColor: Colors.black,
                        unselectedLabelColor: Colors.grey,
                        tabs: [
                          Tab(text: 'Configuration'),
                          Tab(text: 'Preview'),
                          Tab(text: 'Project Structure'),
                        ],
                        onTap: (index) {
                          setState(() {
                            activeTab = index;
                          });
                        },
                      ),
                      Expanded(
                        child: IndexedStack(
                          index: activeTab,
                          children: [
                            // Configuration Tab
                            Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.stretch,
                                children: [
                                  DropdownButtonFormField<String>(
                                    decoration: InputDecoration(labelText: 'Platform'),
                                    value: platform,
                                    onChanged: (value) => setState(() => platform = value!),
                                    items: [
                                      DropdownMenuItem(value: 'android', child: Text('Android')),
                                      DropdownMenuItem(value: 'ios', child: Text('iOS')),
                                    ],
                                  ),
                                  DropdownButtonFormField<String>(
                                    decoration: InputDecoration(labelText: 'Design Pattern'),
                                    value: pattern,
                                    onChanged: (value) => setState(() => pattern = value!),
                                    items: patterns.map((p) => DropdownMenuItem(
                                      value: p['value'],
                                      child: Text(p['label']!),
                                    )).toList(),
                                  ),
                                  TextFormField(
                                    decoration: InputDecoration(labelText: 'Project Name'),
                                    onChanged: (value) => setState(() => projectName = value),
                                  ),
                                  TextFormField(
                                    decoration: InputDecoration(labelText: 'Package Name'),
                                    onChanged: (value) => setState(() => packageName = value),
                                  ),
                                  SizedBox(height: 16.0),
                                  ElevatedButton(
                                    onPressed: projectName.isNotEmpty && packageName.isNotEmpty
                                        ? handleGenerate
                                        : null,
                                    child: Text('Generate Project'),
                                  ),
                                ],
                              ),
                            ),

                            // Preview Tab
                            Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: generatedProject != null
                                  ? Column(
                                crossAxisAlignment: CrossAxisAlignment.stretch,
                                children: [
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.end,
                                    children: [
                                      OutlinedButton.icon(
                                        onPressed: () {
                                          Clipboard.setData(ClipboardData(text: generatedProject!['code']));
                                          showAlert('Code copied to clipboard!');
                                        },
                                        icon: Icon(Icons.copy),
                                        label: Text('Copy Code'),
                                      ),
                                      SizedBox(width: 8.0),
                                      ElevatedButton.icon(
                                        onPressed: handleDownload,
                                        icon: Icon(Icons.download),
                                        label: Text('Download Project'),
                                      ),
                                    ],
                                  ),
                                  SizedBox(height: 16.0),
                                  Expanded(
                                    child: SingleChildScrollView(
                                      child: Container(
                                        padding: EdgeInsets.all(16.0),
                                        decoration: BoxDecoration(
                                          color: Colors.grey[100],
                                          borderRadius: BorderRadius.circular(8.0),
                                        ),
                                        child: Text(
                                          generatedProject!['code'],
                                          style: TextStyle(fontFamily: 'monospace'),
                                        ),
                                      ),
                                    ),
                                  ),
                                ],
                              )
                                  : Center(
                                child: Text(
                                  'Generate a project to see the preview',
                                  style: TextStyle(color: Colors.grey[500]),
                                ),
                              ),
                            ),

                            // Project Structure Tab
                            Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: generatedProject != null
                                  ? Column(
                                crossAxisAlignment: CrossAxisAlignment.stretch,
                                children: [
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    children: [
                                      Text(
                                        'Project Structure',
                                        style: TextStyle(
                                          fontSize: 18.0,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                      ElevatedButton.icon(
                                        onPressed: handleDownload,
                                        icon: Icon(Icons.download),
                                        label: Text('Download Project'),
                                      ),
                                    ],
                                  ),
                                  SizedBox(height: 16.0),
                                  Expanded(
                                    child: Container(
                                      padding: EdgeInsets.all(16.0),
                                      decoration: BoxDecoration(
                                        color: Colors.grey[50],
                                        borderRadius: BorderRadius.circular(8.0),
                                      ),
                                      child: SingleChildScrollView(
                                          child: FileTree(files: generatedProject!['projectFiles'].cast<String, String>())
                                      ),
                                    ),
                                  ),
                                  SizedBox(height: 16.0),
                                  Text(
                                    'Dependencies',
                                    style: TextStyle(
                                      fontSize: 18.0,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                  SizedBox(height: 8.0),
                                  Container(
  padding: EdgeInsets.all(16.0),
  decoration: BoxDecoration(
    color: Colors.grey[50],
    borderRadius: BorderRadius.circular(8.0),
  ),
  child: Column(
    children: generatedProject!['dependencies'].entries.map<Widget>((entry) {
      return Padding(
        padding: const EdgeInsets.symmetric(vertical: 4.0),
        child: Row(
          children: [
            // No icon here!
            SizedBox(width: 8.0), // Optional: Keep some spacing
            Text('${entry.key}:', style: TextStyle(fontFamily: 'monospace')),
            SizedBox(width: 4.0),
            Text(entry.value, style: TextStyle(color: Colors.grey[600])),
          ],
        ),
      );
    }).toList(),
  ),
),
                                ],
                              )
                                  : Center(
                                child: Text(
                                  'Generate a project to see the structure',
                                  style: TextStyle(color: Colors.grey[500]),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
      floatingActionButton: alert != null
          ? Alert(
        message: alert!['message'],
        type: alert!['type'],
        onClose: () {
          setState(() {
            alert = null;
          });
        },
      )
          : null,
    );
  }
}