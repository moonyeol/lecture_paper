
#ifdef __APPLE_CC__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
#include <math.h>
#include <stdio.h>

GLfloat vertices[][3] = {
	{  1.0,  1.0,  1.0 },			//0
	{  1.0, 0.0, 1.0 },			//1
	{  0.0, 0.0, 1.0 },			//2
	{  0.0, 1.0, 1.0 },			//3
	{  0.5, 1.5, 1.0 },			//4
	{  0.0, 1.0, 0.0 },			//5
	{  0.5, 1.5, 0.0 },			//6
	{  1.0, 1.0, 0.0 },			//7
	{  0.0, 0.0, 0.0 },			//8
	{  1.0, 0.0, 0.0 },			//9
	{  -0.2, 0.8, 1.0 },			// 10
	{  -0.2, 0.8, 0.0 },			// 11
	{  1.2, 0.8, 0.0 },			// 12
	{  1.2, 0.8,  1.0 }   		// 13
};

GLfloat colors[][3] = {
	{ 1.0, 0.0, 0.0 },      // red 
	{ 1.0, 1.0, 0.0 },      // yellow 
	{ 0.0, 1.0, 0.0 },      // green 
	{ 0.0, 0.0, 1.0 },      // blue 
	{ 1.0, 1.0, 1.0 },      // white 
	{ 1.0, 0.0, 1.0 } };     // magenta 

void rect(int a, int b, int c, int d)
{
	glEnable(GL_POLYGON_OFFSET_FILL); // Avoid Stitching!
	glPolygonOffset(1.0, 1.0);
	glColor3fv(colors[3]);

	glBegin(GL_POLYGON);
	glVertex3fv(vertices[a]);
	glVertex3fv(vertices[b]);
	glVertex3fv(vertices[c]);
	glVertex3fv(vertices[d]);
	glEnd();

	glDisable(GL_POLYGON_OFFSET_FILL);
	glColor3fv(colors[4]);
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	glBegin(GL_POLYGON);
	glVertex3fv(vertices[a]);
	glVertex3fv(vertices[b]);
	glVertex3fv(vertices[c]);
	glVertex3fv(vertices[d]);
	glEnd();

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
}
void pentagon(int a, int b, int c, int d, int e)
{
	//���� �׸�
	glEnable(GL_POLYGON_OFFSET_FILL); // Avoid Stitching!
	glPolygonOffset(1.0, 1.0);

	glColor3fv(colors[1]);
	glBegin(GL_POLYGON);
	glVertex3fv(vertices[a]);
	glVertex3fv(vertices[b]);
	glVertex3fv(vertices[c]);
	glVertex3fv(vertices[d]);
	glVertex3fv(vertices[e]);
	glEnd();
	//��輱 �׸�
	glDisable(GL_POLYGON_OFFSET_FILL);
	glColor3fv(colors[4]);
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	glBegin(GL_POLYGON);
	glVertex3fv(vertices[a]);
	glVertex3fv(vertices[b]);
	glVertex3fv(vertices[c]);
	glVertex3fv(vertices[d]);
	glVertex3fv(vertices[e]);
	glEnd();

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
}
// 7���� ���� �����. 
void createModel(void)
{
	pentagon(3, 4, 0, 1, 2);    // front 
	pentagon(5, 6, 7, 9, 8);    // back 
	rect(6, 4, 10, 11);    // top_left 
	rect(4, 6, 12, 13);    // top_right 
	rect(7, 9, 1, 0);    // right 
	rect(8, 9, 1, 2);    // bottom 
	rect(5, 3, 2, 8);    // left
}

void init(void)
{
	glClearColor(0.0, 0.0, 0.0, 0.0);
	glColor3f(1.0, 1.0, 0.0);
	glEnable(GL_DEPTH_TEST);    // ���� Ȱ��ȭ 
}
//�ݺ��� ���̱� ���� ���⼭ ����
void reshape(int w, int h)
{
	glViewport(0, 0, w, h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0.0, 2.0, 0.0, 2.0, 4.0, 5.0);//�亼�� ����
}
void display(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);

	glLoadIdentity();
	gluLookAt(0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	//(���� �� ���ڴ� ī�޶��� ��ġ,
	//�߰��� �� ���ڴ� ī�޶��� ����(�ٶ󺸴� ��, ������� ��������), 
	//������ �����ڴ� ī�޶� �Ӹ��� ����(������� �������),

	createModel();   // �� ���� 

	glFlush();
	glutSwapBuffers();
}
// �������, ���� �������� ���� �ۼ��մϴ�.
void main(int argc, char** argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(400, 400);
	glutCreateWindow("House");
	init();
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutIdleFunc(display);
	glutMainLoop();
}

