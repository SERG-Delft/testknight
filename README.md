\documentclass{article}

\usepackage[utf8]{inputenc}
\usepackage[english]{babel}
\usepackage[margin=2.5cm]{geometry}

\usepackage{comment}

\usepackage[backend=biber]{biblatex}
\addbibresource{refs.bib}

\begin{document}

\begin{titlepage}
   \begin{center}
        \vspace*{1cm}
       
        \vspace{0.5cm}
        \large CSE200 Software Project
        \vspace{0.5cm}

        \textbf{\huge TestBuddy: Project Plan}
       
        \vspace{0.5cm}
       
        April 2021

        \vspace{1.5cm}

        Pavlos Makridis \\ 
        Cristian-Alexandru Botocan \\
        Jorge Romeu Huidobro\\
        Mathan Sundarrajan \\
        Piyush Deshmukh

        \vspace{1.5cm}
        %\vfill
            
        \textbf{Client:} Software Engineering Research Group (SERG)\\
        \textbf{TU Coach:} Can Umut Ileri\\
        \textbf{TA:} Bar Lerer\\
            
   \end{center}
\end{titlepage}

\newpage
\tableofcontents
\newpage

\section{Problem Analysis}
\subsection{Problem Statement}
\subsection{Project Goals}
\subsection{Research on Existing Products}
\subsection{The Data Structure}
\subsection{Use Case Analysis}

\section{Requirements Elicitation}
 
  Moreover, since we worked with Java and we have to do a powerful testing procedure to find the bugs using the IntelIJ IDE, we include our experience in the testing phase. Thus, we could also think about how we can make the life of developers easier for the testing phase. While we took a look at other plugins and IntelIJ Junit5 functionalities which were specialized into the testing phase, we could observe what those offers and if we can come with some improved features or at least use some similar functionalities.For instance we could use the system that Junit5 uses for warn developer if he/she wants to keep the coverage from the previous run, or he/she wants to merge the new coverage value into the old one. Another relevant example can be represented by the type of the UI is used.
\subsection{Interview Based Requirements Engineering}
\subsection{Crowd - Based Requirements Engineering}
\subsection{MoSCoW Method}

\section{Feasibility Study}
\subsection{Technical Feasibility}
\subsection{Operational Feasibility}
\subsection{Schedule Feasibility}
\subsection{Summary Feasibility}


\section{Risk Analysis}
\subsection{Limited Communication Abilities}
\subsection{Sudden Growth in Requirements}
\subsection{Dependency on....}
\subsection{Lack of Knowledge in Law About Data Collection}
\subsection{Lack of Experience with UI/UX Design}
\subsection{Lack of Experience with Developing IntellIJ Plugins}

\section{Solution Proposal}
\subsection{Architecture}

\section{General Planning}
\subsection{Processes}
\subsection{Tools}
\subsection{Project Schedule}

\section{Reference List}
\newpage
\section{Appendices}

\subsection{The Data Structure}

\subsection{Use cases}
\subsubsection{Developer Use Cases}

\subsection{Survey}
\subsubsection{Questions}
\subsubsection{Result}


\subsection{Requirements}
The following is a list of all the requirements that have been engineered for TestBuddy product.
\subsubsection{Non-Functional}
\begin{itemize}
 \item The coverage information is not persistent (for now).
 \item  The code should be written in a way so that it is easy to add
support for new criteria to generate checklist items.
 \item The code should be written in a way so that it is easy to add
support to detect new test code smells.
\end{itemize}


\subsubsection{Must Have}
\begin{itemize}
\item The user must be able to quickly select a test to duplicate.
\item The duplicated test will have the important segments highlighted. The user will
be able to jump and edit the important segments of the duplicated test by
pressing \textit{ENTER}. The user can break out of this suggestion cycle by pressing
\textit{ESC}.
\item The user must be able to request to see a \textbf{diff} view, showcasing the
difference in coverage between 2 consecutive runs of a test suite.
\item The user must be able to see how the current coverage compares to the previous
coverage (coverage gained and lost). This information should be displayed in a
non-intrusive manner (no pop-ups).
\item The user must generate the coverage report when selecting the “run with
coverage” option.
\item The user must be able to run a test suite with coverage and then request to see
what lines a specific test covers (on its own). At the minimum, this should
work for failing tests.
\item The user must be able to see recommendations regarding the assertions they need
to write based on the Acting part of the test. These recommendations should
appear in the UI.
\item The user must be able to select a part of the source code and request to get a
checklist for test cases. For example, a checklist generated over a loop should
suggest test cases for 0,1 and more iterations of that loop.
\item Common actions must have a keyboard shortcut bound.
\end{itemize}
\subsubsection{Should Have}
\subsubsection{Could Have}
\subsubsection{Won't Have}

\subsection{Project Schedule}

\printbibliography
\end{document}
