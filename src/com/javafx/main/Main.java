package com.javafx.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.javafx.bd.ConnectionFactory;
import com.javafx.modelo.Pessoa;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	private Button btnAtualizar;
	private Button btnExcluir;
	private Button btnExibir;
	private Button btnLogin;
	private Button btnLogout;
	private Button btnPesquisarImagem;
	private Button btnSalvar;
	private CheckBox cb1, cb2, cb3;
	private ComboBox cbNomes;
	private Connection conexao;
	private DatePicker dpAniversario;
	private Desktop desktop = Desktop.getDesktop();
	private File file;
	private FileChooser fileChooser;
	private FileInputStream fis;
	private HBox hb_DatePicker_PesquisaImagem;
	private HBox hb_ID_Nome;
	private Image image;
	private ImageView imageView = new ImageView();
	private Label label;
	private ListView lvNomes;
	private ObservableList cbList = FXCollections.observableArrayList();
	private ObservableList options = FXCollections.observableArrayList();
	private ObservableList<Pessoa> dataTable;
	private PasswordField password;
	private PreparedStatement stmt;
	private RadioButton rbHomem;
	private RadioButton rbMulher;
	private ResultSet rs;
	private String radioButtonLabel = "Homem";
	private TableView<Pessoa> table;
	private TextArea textArea;
	private TextField tfEmail;
	private TextField tfID;
	private TextField tfNome;
	private TextField tfPassword;
	private TextField tfPesquisaTabela;
	private TextField tfSobrenome;
	private TextField tfTelefone;
	private TextField tfUsername;
	private TextField username;
	private ToggleGroup tgSexo;
	private VBox vboxLogin;

	@Override
	public void start(Stage primaryStage) {

		// inicializa a conexao com banco de dados
		conexao = ConnectionFactory.getConnection();

		// inicializa os valores do ComboBox no inicio da aplicação
		carregaComboBox();

		// Stage
		primaryStage.setTitle("JavaFX - MySQL - Lista de Pessoas");
		primaryStage.initStyle(StageStyle.DECORATED);

		// Tela 1 - Login
		Group group = new Group();
		Scene scene = new Scene(group, 300, 200, Color.rgb(255, 0, 255, 0.));

		// ------------------------------------------------
		// Interface Grafica da Tela de Login
		initLayoutLogin(group);

		// Tela 2 - Menus
		BorderPane tela2 = new BorderPane();
		Scene newScene = new Scene(tela2, Color.rgb(255, 0, 255, 0.5));

		// Eventos da Tela de Login
		initEventsLogin(primaryStage, newScene);

		// Interface Grafica do Topo da Tela
		initLayoutTopo(primaryStage, scene, tela2);

		// Layout Esquerdo
		initLayoutEsquerdo(primaryStage, scene, tela2);

		// Interface Grafica do Centro da Tela
		initLayoutCentro(tela2);

		// ------------------------------------------------
		// Eventos do Layout Esquerdo
		initEventsEsquerdo(primaryStage, tela2);

		// Eventos do Layout Centro
		initEventsCentro(primaryStage);

		// Eventos do Layout Topo
		initEventsTopo(primaryStage, scene);

		// ------------------------------------------------
		// primaryStage.setScene(scene);
		primaryStage.setScene(newScene);
		// primaryStage.setMaximized(false);
		primaryStage.resizableProperty().setValue(Boolean.FALSE);
		primaryStage.show();
	}// start

	private void initEventsTopo(Stage primaryStage, Scene scene) {
		btnLogout.setOnAction(e -> {

			// retorna a tela de Login
			primaryStage.setScene(scene);
			primaryStage.show();
		});

		FilteredList<Pessoa> filteredData = new FilteredList<>(dataTable, e -> true);
		tfPesquisaTabela.setOnKeyPressed(e -> {
			tfPesquisaTabela.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredData.setPredicate(user -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}

					String lowerCaseFilter = newValue.toLowerCase();
					if (Integer.toString(user.getId()).contains(newValue)) {
						return true;
					} else if (user.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}

					return false;
				});
			});
			SortedList<Pessoa> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(table.comparatorProperty());
			table.setItems(sortedData);
		});
	}

	private void initLayoutTopo(Stage primaryStage, Scene scene, BorderPane tela2) {
		btnLogout = new Button("Logout");
		btnLogout.setFont(Font.font("SanSerif", 15));
		btnLogout.setFocusTraversable(false);

		tfPesquisaTabela = new TextField();
		tfPesquisaTabela.setFont(Font.font("SanSerif", 15));
		tfPesquisaTabela.setPromptText("Pesquisar...");
		tfPesquisaTabela.setPrefWidth(805);
		tfPesquisaTabela.setFocusTraversable(true);

		HBox hbPesquisaLogout = new HBox(18);
		hbPesquisaLogout.setAlignment(Pos.TOP_RIGHT);
		hbPesquisaLogout.setPadding(new Insets(10));
		hbPesquisaLogout.getChildren().addAll(tfPesquisaTabela, btnLogout);

		// definindo componentes no topo da Aplicação
		tela2.setTop(hbPesquisaLogout);
		BorderPane.setAlignment(btnLogout, Pos.TOP_RIGHT);
		BorderPane.setMargin(btnLogout, new Insets(0, 0, 10, 0));
	}

	private void initEventsCentro(Stage primaryStage) {
		table.setOnMouseClicked(e -> {
			selecionarCamposTabela();
		});

		table.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
				selecionarCamposTabela();
			}
		});

		// Tela de Explorer para Selecionar Arquivos
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"),
				new ExtensionFilter("Audio Files", "*.wav", "*.mp3"),
				new ExtensionFilter("Image Files", "*.png", "*jpg", "*.gif"),
				new ExtensionFilter("TextFiles", "*.txt"));

		// Button - Pesquisar Imagem
		btnPesquisarImagem.setOnAction(e -> {
			// Seleção unica de arquivos
			file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				try {
					// desktop.open(file);
					textArea.setText(file.getAbsolutePath());
					image = new Image(file.toURI().toString(), 145, 145, true, true);// path,
																						// PrefWith,
																						// PrefHeight,
																						// PreserveRatio,
																						// Smooth
					imageView.setImage(image);
					imageView.setFitWidth(145);
					imageView.setFitHeight(145);
					imageView.setPreserveRatio(true);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			// Selção Multipla de Arquivos
			// List<File> fileList =
			// fileChooser.showOpenMultipleDialog(primaryStage);
			// if (fileList != null) {
			// fileList.stream().forEach(selectedFiles -> {
			// try {
			// // desktop.open(selectedFiles);
			// textArea.setText(fileList.toString());
			// } catch (Exception e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// });
			// }
		});
	}

	private void initEventsEsquerdo(Stage primaryStage, BorderPane tela2) {

		// Button - Salvar
		btnSalvar.setOnAction(e -> {
			// validateNumber() & validateFields()&validateFirstName() &
			// validateLastName() & validateEmail() &
			// validatePassword()&validateMobile()
			// if (true) {
			try {
				String sql = "insert into userdatabase "
						+ "(firstname, lastname, email, username, password, dob, gender,mobile, hobbies)"
						+ "values (?, ?, ?, ?, ?,?,?,?, ?)";

				stmt = conexao.prepareStatement(sql);

				stmt.setString(1, tfNome.getText());
				stmt.setString(2, tfSobrenome.getText());
				stmt.setString(3, tfEmail.getText());
				stmt.setString(4, tfUsername.getText());
				stmt.setString(5, tfPassword.getText());
				stmt.setString(6, dpAniversario.getEditor().getText());
				stmt.setString(7, radioButtonLabel);
				stmt.setString(8, tfTelefone.getText());
				stmt.setString(9, cbList.toString());

				stmt.execute();

				// salvar Imagem na base de dados (ERRO)
				// fis = new FileInputStream(file);
				// stmt.setBinaryStream(10, fis, (int) file.length());

				// mensagem avisando que os dados foram salvos com sucesso
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Salvar Contatos");
				alert.setHeaderText(null);
				alert.setContentText("Usuario salvo com sucesso !!!");
				alert.showAndWait();

				stmt.close();
				rs.close();
			} catch (Exception e2) {

				// mensagem de erro
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Salvar Contatos");
				alert.setHeaderText("Erro ao Salvar Usuario !!!");
				alert.setContentText(e2.getLocalizedMessage());
				alert.showAndWait();
			}
			limparTextFields();
			carregaComboBox();
			carregarTabela();
			// }
		});

		// Button - Exibir
		btnExibir.setOnAction(e -> {
			limparTextFields();
			carregarTabela();
			carregaComboBox();
		});

		// Button - Excluir
		btnExcluir.setOnAction(e -> {
			// Exibir mensagem se deseja excluir os dados
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Excluir Arquivos");
			alert.setHeaderText("Tem certeza que deseja excluir o arquivo selecionado?");
			alert.setContentText(null);

			Optional<ButtonType> action = alert.showAndWait();

			// se SIM
			if (action.get() == ButtonType.OK) {
				try {
					String sql = "delete from userdatabase where id=?";
					stmt = conexao.prepareStatement(sql);
					stmt.setString(1, tfID.getText());
					stmt.executeUpdate();
					stmt.close();
					limparTextFields();
				} catch (Exception e1) {

					// mensagem de erro
					Alert alertExcluir = new Alert(AlertType.ERROR);
					alertExcluir.setTitle("Excluir Arquivos");
					alertExcluir.setHeaderText("Erro ao Excluir !!!");
					alertExcluir.setContentText(e1.getLocalizedMessage());
					alertExcluir.showAndWait();
				}
			}
			carregaComboBox();
			carregarTabela();
		});

		// Button - Atualizar
		btnAtualizar.setOnAction(e -> {
			try {
				String sql = "update userdatabase set firstname=?, lastname=?, "
						+ "email=?, username=?, password=?, dob=?, gender=?, mobile=?, hobbies=? " + "where id='"
						+ tfID.getText() + "'";
				stmt = conexao.prepareStatement(sql);

				stmt.setString(1, tfNome.getText());
				stmt.setString(2, tfSobrenome.getText());
				stmt.setString(3, tfEmail.getText());
				stmt.setString(4, tfUsername.getText());
				stmt.setString(5, tfPassword.getText());
				stmt.setString(6, dpAniversario.getEditor().getText());
				stmt.setString(7, radioButtonLabel);
				stmt.setString(8, tfTelefone.getText());
				stmt.setString(9, cbList.toString());

				// recuperar Imagem na base de dados (ERRO)
				// fis = new FileInputStream(file);
				// stmt.setBinaryStream(10, fis, (int) file.length());

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Usuario Atulizado");
				alert.setHeaderText(null);
				alert.setContentText("Usuario Alterado com Sucesso");
				alert.showAndWait();

				stmt.execute();

				stmt.close();
				rs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			limparTextFields();
			carregaComboBox();
			carregarTabela();
			// }
		});

		// ComboBox - Lista de Nomes
		cbNomes.setOnAction(e -> {
			try {
				String sql = "select * from userdatabase where firstname=?";
				stmt = conexao.prepareStatement(sql);
				stmt.setString(1, (String) cbNomes.getSelectionModel().getSelectedItem());
				rs = stmt.executeQuery();

				while (rs.next()) {
					updateData();
				}

				stmt.close();
				rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		// RadioButton - Sexo
		rbHomem.setOnAction(e -> {
			radioButtonLabel = rbHomem.getText();
		});
		rbMulher.setOnAction(e -> {
			radioButtonLabel = rbMulher.getText();
		});

		// ListView - Nomes
		lvNomes.setOnMouseClicked(e -> {
			try {
				String sql = "select * from userdatabase where firstname=?";
				stmt = conexao.prepareStatement(sql);
				stmt.setString(1, (String) lvNomes.getSelectionModel().getSelectedItem());
				rs = stmt.executeQuery();

				while (rs.next()) {
					updateData();
				}

				stmt.close();
				rs.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	private void initLayoutEsquerdo(Stage primaryStage, Scene scene, BorderPane tela2) {

		// Label - Criar Usuario
		Label lbPessoa = new Label("Criar Usuario");
		lbPessoa.setFont(Font.font("SanSerif", 20));

		// TextField - ID
		tfID = new TextField();
		tfID.setFont(Font.font("SanSerif", 20));
		tfID.setPromptText("ID");
		tfID.setMaxWidth(45);
		tfID.setEditable(false);
		tfID.setFocusTraversable(false);

		// TextField - Nome
		tfNome = new TextField();
		tfNome.setFont(Font.font("SanSerif", 20));
		tfNome.setPromptText("Nome");
		tfNome.setMaxWidth(250);

		// TextField -Sobrenome
		tfSobrenome = new TextField();
		tfSobrenome.setFont(Font.font("SanSerif", 20));
		tfSobrenome.setPromptText("Sobrenome");
		tfSobrenome.setMaxWidth(300);

		// TextField - Email
		tfEmail = new TextField();
		tfEmail.setFont(Font.font("SanSerif", 20));
		tfEmail.setPromptText("Email");
		tfEmail.setMaxWidth(300);

		// TextField - Username
		tfUsername = new TextField();
		tfUsername.setFont(Font.font("SanSerif", 20));
		tfUsername.setPromptText("Usuario");
		tfUsername.setMaxWidth(300);

		// TextField/PasswordField - Senha
		/* tfPassword = new PasswordField(); */
		tfPassword = new TextField();
		tfPassword.setFont(Font.font("SanSerif", 20));
		tfPassword.setPromptText("Senha");
		tfPassword.setMaxWidth(300);

		// TextField - Telefone
		tfTelefone = new TextField();
		tfTelefone.setFont(Font.font("SanSerif", 20));
		tfTelefone.setPromptText("Telefone");
		tfTelefone.setMaxWidth(300);

		// DatePicker - Data de Aniversário
		dpAniversario = new DatePicker(LocalDate.now());
		dpAniversario.setStyle("-fx-color: orange;" + "-fx-font-size:20;");
		// dpData.setPromptText("Data de Aniversário");
		dpAniversario.setPrefWidth(200);

		// Button - Salvar
		btnSalvar = new Button("Salvar");
		btnSalvar.setPrefWidth(70);
		btnSalvar.setFont(Font.font("SanSerif", 15));

		// Button - Exibir
		btnExibir = new Button("Exibir");
		btnExibir.setPrefWidth(65);
		btnExibir.setFont(Font.font("SanSerif", 15));

		// Button - Excluir
		btnExcluir = new Button("Excluir");
		btnExcluir.setPrefWidth(70);
		btnExcluir.setFont(Font.font("SanSerif", 15));

		// Button - Atualizar
		btnAtualizar = new Button("Atualizar");
		btnAtualizar.setPrefWidth(80);
		btnAtualizar.setFont(Font.font("SanSerif", 15));

		// HBox - Salvar, Exibir, Excluir, Atualizar
		HBox hbBotoesCRUD = new HBox(5);
		hbBotoesCRUD.setPadding(new Insets(5, 0, 0, 0));
		hbBotoesCRUD.getChildren().addAll(btnSalvar, btnExibir, btnExcluir, btnAtualizar);

		// Button - Pesquisar Imagens
		btnPesquisarImagem = new Button("Pesquisar IMG");
		btnPesquisarImagem.setWrapText(true);
		btnPesquisarImagem.setPrefWidth(95);
		btnPesquisarImagem.setPrefHeight(44);
		btnPesquisarImagem.setFont(Font.font("SanSerif", 12));
		btnPesquisarImagem.setFocusTraversable(false);

		// ComboBox - Nomes
		cbNomes = new ComboBox(options);
		cbNomes.setPrefWidth(150);
		cbNomes.setValue("Lista de Nomes");

		// Radio Button - Sexo
		tgSexo = new ToggleGroup();
		rbHomem = new RadioButton("Homem");
		rbHomem.setToggleGroup(tgSexo);
		rbHomem.setSelected(true);
		rbMulher = new RadioButton("Mulher");
		rbMulher.setToggleGroup(tgSexo);

		// ListView - Nomes
		lvNomes = new ListView(options);
		lvNomes.setMaxSize(145, 145);

		// CheckBox - Tipos
		cb1 = new CheckBox("Basquete");
		cb1.setOnAction(e -> {
			cbList.add(cb1.getText());
		});
		cb2 = new CheckBox("Corrida");
		cb2.setOnAction(e -> {
			cbList.add(cb2.getText());
		});
		cb3 = new CheckBox("Skate");
		cb3.setOnAction(e -> {
			cbList.add(cb3.getText());
		});

		// unir campos DatePicker e Pesquisar Imagem
		hb_DatePicker_PesquisaImagem = new HBox(5);
		hb_DatePicker_PesquisaImagem.getChildren().addAll(dpAniversario, btnPesquisarImagem);

		// unir campos ID e Nome na mesma linha
		hb_ID_Nome = new HBox(5);
		hb_ID_Nome.getChildren().addAll(tfID, tfNome);

		// VBox - uniao dos campos de texto
		VBox fields = new VBox(5);
		fields.setPadding(new Insets(0, 10, 0, 0));
		fields.setAlignment(Pos.TOP_CENTER);
		fields.getChildren().addAll(lbPessoa, hb_ID_Nome, tfSobrenome, tfEmail, tfUsername, tfPassword, tfTelefone,
				hb_DatePicker_PesquisaImagem, hbBotoesCRUD);

		// definindo que os valores fiquem localizados no lado esquerdo
		tela2.setLeft(fields);

		HBox hb_cbTipos = new HBox(50);
		hb_cbTipos.setPadding(new Insets(5));
		hb_cbTipos.setAlignment(Pos.CENTER);
		hb_cbTipos.getChildren().addAll(cb1, cb2, cb3);

		HBox hb_cb_radio = new HBox(10);
		hb_cb_radio.getChildren().addAll(cbNomes, rbHomem, rbMulher);
		hb_cb_radio.setAlignment(Pos.CENTER_LEFT);

		HBox hb_lv_imgview = new HBox(5);
		hb_lv_imgview.getChildren().addAll(lvNomes, imageView);

		fields.getChildren().addAll(hb_cb_radio, hb_lv_imgview, hb_cbTipos);
	}

	private void initLayoutCentro(BorderPane tela2) {
		// Lista de dados para Tabela
		dataTable = FXCollections.observableArrayList();

		table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setTableMenuButtonVisible(true);
		table.setPrefHeight(550);

		TableColumn column1 = new TableColumn("ID");
		column1.setPrefWidth(50);
		column1.setCellValueFactory(new PropertyValueFactory("id"));

		TableColumn column2 = new TableColumn("Nome");
		column2.setPrefWidth(100);
		column2.setCellValueFactory(new PropertyValueFactory("firstName"));

		TableColumn column3 = new TableColumn("Sobrenome");
		column3.setPrefWidth(100);
		column3.setCellValueFactory(new PropertyValueFactory("lastName"));

		TableColumn column4 = new TableColumn("Email");
		column4.setPrefWidth(100);
		column4.setCellValueFactory(new PropertyValueFactory("email"));

		TableColumn column5 = new TableColumn("Username");
		column5.setPrefWidth(80);
		column5.setCellValueFactory(new PropertyValueFactory("username"));

		TableColumn column6 = new TableColumn("Password");
		column6.setPrefWidth(80);
		column6.setCellValueFactory(new PropertyValueFactory("password"));

		TableColumn column7 = new TableColumn("Telefone");
		column7.setPrefWidth(100);
		column7.setCellValueFactory(new PropertyValueFactory("mobile"));

		TableColumn column8 = new TableColumn("Aniversário");
		column8.setPrefWidth(100);
		column8.setCellValueFactory(new PropertyValueFactory("dob"));

		TableColumn column9 = new TableColumn("Sexo");
		column9.setPrefWidth(80);
		column9.setCellValueFactory(new PropertyValueFactory("gender"));

		TableColumn column10 = new TableColumn("Hobbies");
		column10.setPrefWidth(100);
		column10.setCellValueFactory(new PropertyValueFactory("hobbies"));

		table.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, column8, column9,
				column10);

		carregarTabela();

		ScrollPane sp = new ScrollPane(table);
		// sp.setContent(table);
		sp.setPrefSize(800, 200);
		sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		sp.setFitToHeight(true);
		sp.setHmax(3);
		sp.setHvalue(0);

		textArea = new TextArea();
		textArea.setFont(Font.font("SanSerif", 20));
		textArea.setPromptText("Diretório dos arquivos selecionados");
		textArea.setPrefSize(300, 50);
		textArea.setEditable(false);

		VBox vb_Tabela_TextArea = new VBox(5);
		vb_Tabela_TextArea.getChildren().addAll(table, textArea);

		// definindo a localização da tabela no centro do aplicativo
		tela2.setCenter(vb_Tabela_TextArea);
		tela2.setPadding(new Insets(10));
	}

	private void initEventsLogin(Stage primaryStage, Scene newScene) {
		password.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				try {
					String sql = "select * from userdatabase where username=? and password=?";
					stmt = conexao.prepareStatement(sql);
					stmt.setString(1, username.getText());
					stmt.setString(2, password.getText());

					rs = stmt.executeQuery();

					if (rs.next()) {
						label.setText("Login Success ");
						primaryStage.setScene(newScene);
						primaryStage.show();
						username.clear();
						password.clear();
					} else {
						label.setText("Login Failed");
						// username.clear();
						password.clear();
					}

					stmt.close();
					rs.close();

				} catch (Exception e2) {
					e2.printStackTrace();
					label.setText("SQL Erro");
					System.err.println(e2);
				}
			}
		});

		btnLogin.setOnAction(e -> {
			try {
				String sql = "select * from userdatabase where username=? and password=?";
				stmt = conexao.prepareStatement(sql);
				stmt.setString(1, username.getText());
				stmt.setString(2, password.getText());

				rs = stmt.executeQuery();

				if (rs.next()) {
					label.setText("Login Success ");
					primaryStage.setScene(newScene);
					primaryStage.show();
					username.clear();
					password.clear();
				} else {
					label.setText("Login Failed");
					// username.clear();
					password.clear();
				}

				stmt.close();
				rs.close();

			} catch (Exception e2) {
				e2.printStackTrace();
				label.setText("SQL Erro");
				System.err.println(e2);
			}
		});
	}

	private void initLayoutLogin(Group group) {
		label = new Label("Login Screen");
		label.setFont(new Font("SanSerif", 20));

		username = new TextField();
		username.setFont(Font.font("SanSerif", 20));
		username.setPromptText("Username");
		username.getStyleClass().add("field-background");
		username.setFocusTraversable(false);

		password = new PasswordField();
		password.setFont(Font.font("SanSerif", 20));
		password.setPromptText("Password");
		password.getStyleClass().add("field-background");
		password.setFocusTraversable(false);

		btnLogin = new Button("Login");
		btnLogin.setFont(Font.font("SanSerif", 15));
		btnLogin.setFocusTraversable(true);

		Color foreground = Color.rgb(255, 255, 255, 0.9);
		Rectangle background = new Rectangle(300, 200);
		background.setX(0);
		background.setY(0);
		background.setArcHeight(15);
		background.setArcWidth(15);
		background.setFill(Color.rgb(0, 0, 0, 0.55));
		background.setStroke(foreground);
		background.setStrokeWidth(1.5);

		vboxLogin = new VBox(5);
		vboxLogin.setPadding(new Insets(10));
		vboxLogin.getChildren().addAll(label, username, password, btnLogin);

		group.getChildren().addAll(background, vboxLogin);
	}

	private void selecionarCamposTabela() {
		Pessoa user = table.getSelectionModel().getSelectedItem();
		try {
			String sql = "select * from userdatabase where id=?";
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, Integer.toString(user.getId()));
			rs = stmt.executeQuery();

			while (rs.next()) {
				updateData();
			}

			stmt.close();
			rs.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void updateData() throws SQLException {
		tfID.setText(rs.getString("id"));
		tfNome.setText(rs.getString("firstname"));
		tfSobrenome.setText(rs.getString("lastname"));
		tfEmail.setText(rs.getString("email"));
		tfUsername.setText(rs.getString("username"));
		tfPassword.setText(rs.getString("password"));
		tfTelefone.setText(rs.getString("mobile"));
		dpAniversario.getEditor().setText(rs.getString("dob"));

		if ("Homem".equalsIgnoreCase(rs.getString("gender"))) {
			rbHomem.setSelected(true);
		} else if ("Mulher".equalsIgnoreCase(rs.getString("gender"))) {
			rbMulher.setSelected(true);
		}
	}

	@SuppressWarnings("unchecked")
	public void carregaComboBox() {
		options.clear();
		try {
			String sql = "select firstname from userdatabase";
			stmt = conexao.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				options.add(rs.getString("firstname"));
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}

	}

	public void carregarTabela() {
		dataTable.clear();
		try {
			String sql = "select * from userdatabase";
			stmt = conexao.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dataTable.add(new Pessoa(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
						rs.getString("email"), rs.getString("username"), rs.getString("password"),
						rs.getString("mobile"), rs.getString("dob"), rs.getString("gender"), rs.getString("hobbies")));
			}
			table.setItems(dataTable);

		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

	private boolean validateFields() {

		if (tfNome.getText().isEmpty() | tfSobrenome.getText().isEmpty() | tfEmail.getText().isEmpty()
				| tfUsername.getText().isEmpty() | tfPassword.getText().isEmpty()) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation Fields");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Into the Fields");
			alert.showAndWait();
			return false;
		}
		if (dpAniversario.getEditor().getText().isEmpty()) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation Fields");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Date");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	private void limparTextFields() {
		tfID.clear();
		tfNome.clear();
		tfSobrenome.clear();
		tfEmail.clear();
		tfUsername.clear();
		tfPassword.clear();
		dpAniversario.setValue(LocalDate.now());
		// comboBox.setValue(options.get(0));
		tfTelefone.clear();
	}

	private boolean validateNumber() {
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(tfID.getText());
		if (m.find() && m.group().equals(tfID.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation Numbers");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Valid Number");
			alert.showAndWait();
			return false;
		}
	}

	private boolean validateFirstName() {
		Pattern p = Pattern.compile("[a-zA-Z]+");
		Matcher m = p.matcher(tfNome.getText());
		if (m.find() && m.group().equals(tfNome.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation First Name");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Valid First Name");
			alert.showAndWait();
			return false;
		}
	}

	private boolean validateLastName() {
		Pattern p = Pattern.compile("[a-zA-Z]+");
		Matcher m = p.matcher(tfSobrenome.getText());
		if (m.find() && m.group().equals(tfSobrenome.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation Last Name");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Valid Last Name");
			alert.showAndWait();
			return false;
		}
	}

	private boolean validateEmail() {

		Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
		Matcher m = p.matcher(tfEmail.getText());
		if (m.find() && m.group().equals(tfEmail.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation Email");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Valid Email");
			alert.showAndWait();
			return false;
		}
	}

	private boolean validateFone() {

		Pattern p = Pattern.compile("");
		Matcher m = p.matcher(tfEmail.getText());
		if (m.find() && m.group().equals(tfEmail.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation Email");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Valid Email");
			alert.showAndWait();
			return false;
		}
	}

	private boolean validatePassword() {

		Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})");
		Matcher m = p.matcher(tfPassword.getText());
		if (m.find() && m.group().equals(tfPassword.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation Password");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Valid Password+\n"
					+ "Password must contain at leat one (Digit, Lowercase, Uppercase, and Special Character) \nand lenght must be between 6-15");
			alert.showAndWait();
			return false;
		}
	}

	private boolean validateMobile() {

		Pattern p = Pattern.compile("(0|91)?[7-9][0-9] {9}");
		Matcher m = p.matcher(tfTelefone.getText());
		if (m.find() && m.group().equals(tfTelefone.getText())) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation Mobile Number");
			alert.setHeaderText("Header text");
			alert.setContentText("Please Enter Valid Mobile Number");
			alert.showAndWait();
			return false;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
