using System;
using System.Threading;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Collections.Generic;

namespace TCPSocket
{
    class EmployeeTCPServer
    {
        static TcpListener listener;
        const int LIMIT = 5; // Количество клиентов 
        static Dictionary<string, string> employees =
        new Dictionary<string, string>()
        {
            {"john", "manager"},
            {"jane", "steno"},
            {"jim", "clerk"},
            {"jack", "salesman"}
        };
        public static void Main()
        {
            //Порт нашего сервера
            Int32 port = 13000;
            //IP-адрес нашего сервера – локальная машина
            IPAddress localAddr = IPAddress.Parse("127.0.0.1");
            listener = new TcpListener(localAddr, port);
            listener.Start();
            #if LOG
                Console.WriteLine("Server mounted, listening to port 13000");
            #endif
            //We would launch our server in a separate thread
            for (int i = 0; i < LIMIT; i++)
            {
                Thread t = new Thread(new ThreadStart(Service));
                t.Start();
            }
        }
        public static void Service()
        {
            //постоянно ждем входящего соединения
            while (true)
            {
                Socket soc = listener.AcceptSocket();
            #if LOG
                Console.WriteLine("Connected: {0}", soc.RemoteEndPoint);
            #endif
                try
                {
                    Stream s = new NetworkStream(soc);
                    StreamReader sr = new StreamReader(s);
                    StreamWriter sw = new StreamWriter(s);
                    sw.AutoFlush = true; // enable automatic flushing
                    
                    sw.WriteLine("Write employees name");
                    sw.WriteLine("{0} Employees available", employees.Count);

                    while (true)
                    {
                        string name = sr.ReadLine();
                        if (name == "" || name == null) break;
                        string job = employees[name];
                        if (job == null) job = "No such employee";
                        sw.WriteLine(job);
                    }
                    s.Close();
                }
                catch (Exception e)
                {
                    #if LOG
                        Console.WriteLine(e.Message);
                    #endif
                }
                #if LOG
                    Console.WriteLine("Disconnected: {0}", soc.RemoteEndPoint);
                #endif
                soc.Close();
            }
        }
    }
}

