package com.example.chat.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.chat.event.LoginEvent;
import com.example.chat.event.MessagesEvent;
import com.example.chat.protocol.Message;
import com.example.chat.protocol.Protocol;
import com.example.chat.service.CommunicationService;
import com.example.chat.service.Statics;

@Component
public class Application extends JFrame implements ApplicationListener<MessagesEvent>{

	private final ApplicationEventPublisher publisher;
	private final StringBuilder burlapBuilder, hessianBuilder;
    private final CommunicationService communicationService;
	
    @Autowired
	public Application(ApplicationEventPublisher publisher, CommunicationService communicationService){
		this.publisher = publisher;
		this.burlapBuilder = new StringBuilder();
		this.hessianBuilder = new StringBuilder();
		this.communicationService = communicationService;
		initApp();
	}
	
	JButton btn, login;
	JTextPane  chatMessages;
	JScrollPane scroll;
	JTextField message, username;
	ButtonGroup btnGroup;
	JRadioButton burlap, hessian;
	private void initApp() {
		
		//wyslij wiadomosc
		btn = new JButton("Send");
		btn.setPreferredSize(new Dimension(80, 20));
		btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
            	sendButtonActionPerformed(event);
            }
		});
		//zaloguj
		login = new JButton("Login");
		login.setPreferredSize(new Dimension(80, 20));
		login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
            	loginButtonActionPerformed(event);
            }
		});
		//pole z wiadomosciami
		chatMessages = new JTextPane();
		chatMessages.setFocusable(false);
		chatMessages.setContentType("text/html");
		chatMessages.setPreferredSize(new Dimension(250, 300));
		scroll = new JScrollPane (chatMessages, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(250, 300));
		
		//pole dla nowej wiadomosci
		message = new JTextField(25);
		//pole na nazwe uzytkownika
		username = new JTextField(10);
		
		//radiobuttony do zmiany technologi
	    btnGroup = new ButtonGroup();
		burlap = new JRadioButton("Burlap");
		burlap.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	burlapRadioActionPerformed(evt);
	            }
		 });
	    hessian = new JRadioButton("Hessian");
	    hessian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	hessianRadioActionPerformed(evt);
            }
	    });
	    btnGroup.add(burlap);
	    btnGroup.add(hessian);

		
		JPanel pane = new JPanel();
		JPanel pane1 = new JPanel();
		JPanel pane2 = new JPanel();
		JPanel pane3 = new JPanel();
		
		BorderLayout grid = new BorderLayout();
		GridLayout flow = new GridLayout(2,2);
		FlowLayout floww = new FlowLayout();
		FlowLayout flowww = new FlowLayout();

		
		pane.setLayout(grid);
		pane1.setLayout(flow);
		pane2.setLayout(floww);
		pane3.setLayout(flowww);

		
		pane1.add(burlap);
		pane1.add(hessian);
		pane1.add(username);
		pane1.add(login);
		
		pane2.add(scroll);
		
		pane3.add(message);
		pane3.add(btn);

		
		pane.add(pane1, BorderLayout.NORTH);
		pane.add(pane2, BorderLayout.CENTER);
		pane.add(pane3, BorderLayout.SOUTH);

		//JFrame fr = new JFrame();
		this.setSize(400, 400);
		this.setContentPane(pane);  
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setLocation(200, 200); 
		this.setVisible(true);      
		
	}
	
	
	//akcja do przycisku logowania
	  private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
	        login.setEnabled(false);
		    LoginEvent loginEvent = new LoginEvent(this, username.getText());
	        publisher.publishEvent(loginEvent);
	  }//GEN-LAST:event_loginButtonActionPerformed
	
	  //zmiana na burlap
	  private void burlapRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_burlapRadioActionPerformed
		  Statics.setSelectedProtocol(Protocol.BURLAP);
		  communicationService.change();
          chatMessages.setText("<html><body>" + burlapBuilder.toString() + "</html></body>");
	  }

	  //zmiana na hessian
	  private void hessianRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hessianRadioActionPerformed
		  chatMessages.setText("");    
		  Statics.setSelectedProtocol(Protocol.HESSIAN);
		  communicationService.change();
          chatMessages.setText("<html><body>" + hessianBuilder.toString() + "</html></body>");
	  }
	  
	  //akcja do wysylania widomosci
	  private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
	        sendMessage();

	  }//GEN-LAST:event_sendButtonActionPerformed
	  
	  private void sendMessage() {
	        String mess = message.getText();
	        communicationService.sendMessage(mess);
	        message.setText("");
	  }
	  
	  
	/*  public void setText(List<Message> messages) {
	        messages.stream().forEach((message) -> {
	            updateMessages(message);      
	            chatMessages.setText("<html><body>" + messagesBuilder.toString() + "</html></body>");
	        });
	    }*/

	    private void updateMessages(Message receivedMessage, StringBuilder messagesBuilder) {
	        boolean myMessage = receivedMessage.getAuthor().equals(Statics.getLoggedUser().getUsername());
	        System.out.println(receivedMessage.getMessage());
	        if (myMessage) {
	            messagesBuilder.append("<strong>");
	        } else {
	            messagesBuilder.append("<span>");
	        }
	        messagesBuilder.append(receivedMessage.getAuthor());
	        messagesBuilder.append(":");
	        if (myMessage) {
	            messagesBuilder.append("</strong>");
	        } else {
	            messagesBuilder.append("</span>");
	        }
	        messagesBuilder.append("<pre>");
	        messagesBuilder.append(receivedMessage.getMessage());
	        messagesBuilder.append("</pre>");
	        messagesBuilder.append("<hr/>");
	}
	  
	private void onMessageEvent(MessagesEvent evnt) {
		
        //System.out.println("taki rozmiar: "+evnt.getMessages().size());
		evnt.getMessages().stream().forEach((message) -> {
			System.out.println("jestemtu w srodku");
			if(Statics.getSelectedProtocol()==Protocol.BURLAP){
	            updateMessages(message,burlapBuilder);
	            chatMessages.setText("<html><body>" + burlapBuilder.toString() + "</html></body>");
			}
			else{
				updateMessages(message,hessianBuilder);
	            chatMessages.setText("<html><body>" + hessianBuilder.toString() + "</html></body>");
			}
	        });
	}

	@Override
	public void onApplicationEvent(MessagesEvent evnt) {
			onMessageEvent(evnt);
	
		
	}
	

	

}