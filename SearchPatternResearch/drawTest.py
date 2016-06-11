import pygame, sys
from pygame.locals import *

pygame.init()
FPS = 30
pygame.display.init()
pygame.display.set_mode((200,200))
CANVAS = pygame.display.get_surface()
fpsClock = pygame.time.Clock()
pygame.display.set_caption('Search Pattern Testing')

agent_game_area = pygame.Rect(50, 50, 100, 100)
agent_view_start_area = pygame.Rect(88, 88, 24, 24)
agent_view_memory = [agent_view_start_area.copy()]
agent_position = [agent_view_start_area.inflate(-24, -24)]
moves = 0

#24, 24, 24, 24
move_list0 = [(1,1), (-1,1), (-1, -1), (1, -1), 
(1, -1), (-1, -1), (-1, 1), (1, 1),
(1, 1), (1, -1), (-1, -1), (-1, 1),
(-1, 1), (-1, -1), (1, -1), (1, 1)]
move_ref0 = [24 * i for i in range(1, 17)]

#12, 24, 24, 24, 12
move_list1 = [(1, 1), (-1, 1), (-1, -1), (1, -1), (1,1),
(1, -1), (-1, -1), (-1, 1), (1, 1), (1, -1),
(1, 1), (1, -1), (-1, -1), (-1, 1), (1, 1),
(-1, 1), (1, 1), (1, -1), (-1, -1), (-1, 1)]

move_ref1 = [12, 36, 60, 84, 96,
108, 132, 156, 180, 192,
204, 228, 252, 276, 288,
300, 324, 348, 372, 384]

move_list2 = [(1, -1), (0, 1), (-1, -1),
(-1, -1), (0, 1), (1, -1)]
move_ref2 = [24, 72, 96,
120, 168, 192]

move_list3 = [(-1, 0), (0, -1), (1, 0), (0, 1), 
(0, 1), (-1, 0), (0, -1), (1, 0),
(1, 0), (0, 1), (-1, 0), (0, -1),
(0, -1), (-1, 0), (0, 1), (1, 0)]
move_ref3 = [24*i for i in range(1,17)]

def draw_helper(moves):
    pygame.draw.rect(CANVAS, pygame.Color("Blue") , agent_game_area)
    for i in agent_view_memory:
        pygame.draw.rect(CANVAS, pygame.Color("Red") , i)
    for i in agent_position:
        pygame.draw.rect(CANVAS, pygame.Color("Green"), i)


def draw1(move_list, move_ref):
    global moves
    draw_helper(moves)
    if moves < move_ref[-1]:
        #how to calculate move list position?
        move_pos = reference_pos(moves, move_ref)
        move_tuple = move_list[move_pos]
        agent_view_memory.append(agent_view_memory[-1].move(move_tuple))
        agent_position.append(agent_position[-1].move(move_tuple))
        moves += 1
    else: 
        pass   

def reference_pos(moves, move_list_ref):
    for i in move_list_ref:
        if moves < i:
            return move_list_ref.index(i)
        else:
            continue

while True :
    for event in pygame.event.get():
        if event.type == QUIT:
            pygame.quit()
            sys.exit()
    draw1(move_list3, move_ref3)
    pygame.display.update()
    fpsClock.tick(FPS)
