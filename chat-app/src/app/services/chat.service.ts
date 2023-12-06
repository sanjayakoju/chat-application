import {Injectable} from '@angular/core';
import {Stomp} from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import {ChatMessage} from '../models/chat-message';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private stompClient: any
  private messageSubject: BehaviorSubject<ChatMessage[]> = new BehaviorSubject<ChatMessage[]>([]);
  baseUrl = '//localhost:8080'

  constructor(private httpClient: HttpClient) {
    this.initConnenctionSocket();
  }

  initConnenctionSocket() {
    const socketUrl = '//localhost:8080/chat-socket';

    const socket = new SockJS(socketUrl);
    this.stompClient = Stomp.over(socket)
  }

  joinRoom(roomId: string) {
    this.stompClient.connect({}, ()=>{
      this.stompClient.subscribe(`/topic/${roomId}`, (messages: any) => {
        const messageContent = JSON.parse(messages.body);
        const currentMessage = this.messageSubject.getValue();
        currentMessage.push(messageContent);

        this.messageSubject.next(currentMessage);

      })
    })
  }

  sendMessage(roomId: string, chatMessage: ChatMessage) {
    this.stompClient.send(`/app/chat/${roomId}`, {}, JSON.stringify(chatMessage))
  }

  getMessageSubject(){
    return this.messageSubject.asObservable();
  }

  getChatHistory(): Observable<ChatMessage> {
    return this.httpClient.get<ChatMessage>(this.baseUrl + `/history`);
  }
}
